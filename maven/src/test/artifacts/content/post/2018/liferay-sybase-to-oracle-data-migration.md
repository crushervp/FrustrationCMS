---
type: 'post'
title: 'Liferay:  Migrating from Sybase to Oracle Using Data Migration'
date: '2018-05-09'
tags: 'liferay, oracle, sybase, data, migration, data migration"
status: 'published'
category: 'Tech Article'
---

I have been working with Liferay EE 6.2 clustered deployment which uses Sybase 15.5 database.  Recently my team
was directed to move from Sybase to Oracle 12C.  Interestingly we migrated from Oracle 11 to Sybase 4 years ago.
During that migration we used the Data Migration page built into Liferay.  We had great success using the built
in functionality.  This page can be found:  Control Panel -> Configuration -> Server Administration - > Data Migration.
<!--summary marker-->
With that experience we decided to use the built in functionality once again. You will need to know your JDBC driver class, URL, username and password.  This connection information is for your target database.  Screenshot below.

![Data Migration Page](/2018/liferay_6.2_control_panel_configuration_server_administration_data_migration.png "Data Migration Page")

Our Liferay deployment connects to multiple databases.  In the previous migration this caused the data migration page to fail.
The first step was to run a migration against our development database.  If your organization doesn't regularly backup your databases, now would be a good time.  After running the migration a several times we identified the following issues:

1.  Oracle only allows table names with lengths up to 30 characters.  A few of our custom tables
    and the Notifications_UserNotificationEvent table exceeded the limit.
2.  Data from Sybase Text columns were being truncated when copied to Oracle CLOB columns.
3.  Data Migration page expected tables from a second database to be in the Liferay
    primary database.  Seeing the tables were not in the primary database the migration
    would stop.
   
The first issue was solved by upgrading to the latest notifications portlet and by shortening the namespace in our service.xml files.  The third issue we solved with a hack.  We only used 20 tables in our external databases.  We created a SQL script to 
create 20 tables without columns that matched the tables in the external databases.  The Data Migration page would see the
empty tables and copy them to the target database.  They can later be dropped.  The second issue was more difficult to
overcome.

The Oracle CLOB columns defaulted to 4k.  We had tables which contained more than 4k worth of data.  During this process
I had been consulting with our DBA.  Using the Liferay [Oracle Database Support Policy](https://web.liferay.com/group/customer/support/home/coverage/installation/databases)  as a guide we discussed the CLOB issue and decided our best course of action would be to 
create the tables in the new database with a SQL script.  I downloaded the source code for Liferay and went through the
source code for our custom portlets.  With the assistance of our DBA I was able to consolidate the SQL files and modify
them to create CLOB columns that met our needs, mostly.  

We still didn't know what data sizes were in our database.  To finish the SQL scripts we needed some metrics to determine
what size to make the CLOB columns.  I hacked together a small JAVA program to scan the database and report on all the
Sybase Text columns.  Below I provide the SQL I used in this program.  

The first is SQL to pull table and column names for every table that had a Text column.

<kodo apudskribo="Find text columns">

```
  select o.name as tableName, c.name as columnName from sysobjects o 
        inner join syscolumns c on c.id = o.id 
        inner join systypes t on t.usertype = c.usertype and t.type = c.type 
        where t.name = 'text' AND o.loginame='my_user' order by o.name;
```
</kodo>

Then using the table and column name run the following queries to find the row count along with the max, min and average sizes of the text columns.

<kodo apudskribo="row count">

```
  select count(*) from table_name
```
</kodo>

<kodo apudskribo="max data size">

```
  select max(datalength(column_name)) from table_name
```
</kodo>

<kodo apudskribo="min data size">

```
  select min(datalength(column_name)) from table_name
```
</kodo>

<kodo apudskribo="avg data size">

```
  select avg(datalength(column_name)) from table_name
```
</kodo>

I then consolidated this data into a text report similar to the following:

<pre>
TableName                     ColumnName       MaxSize  AvgSize  MinSize
--------------------------------------------------------------------------------
AnnouncementsEntry            content          3762     271      7
BackgroundTask                taskContext      151075   94270    78
BackgroundTask                statusMessage    966      56       1
BlogsEntry                    content          10346    1027     2
BlogsEntry                    trackbacks       1        1        1
CalendarBooking               description      710      38       1
CalendarNotificationTemplate  body             0        0        0
CalEvent                      recurrence       6459     482      4

</pre>

With this information I was able to modify the table creation SQL scripts to use CLOB sizes
that should meet our needs.  As an example I have shown the DDL that I used for the 
BackgroundTask table.

<kodo apudskribo="create table with CLOB size specified">

```
create table BackgroundTask (
	backgroundTaskId number(30,0) not null primary key,
	groupId number(30,0),
	companyId number(30,0),
	userId number(30,0),
	userName VARCHAR2(75 CHAR) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name VARCHAR2(75 CHAR) null,
	servletContextNames VARCHAR2(255 CHAR) null,
	taskExecutorClassName VARCHAR2(200 CHAR) null,
	taskContext clob null,
	completed number(1, 0),
	completionDate timestamp null,
	status number(30,0),
	statusMessage clob null
)
LOB ( taskContext ) STORE AS ( TABLESPACE IKROME_LOB_DATA STORAGE ( INITIAL 256K NEXT 256K ) CHUNK 8192 NOCACHE LOGGING PCTVERSION 0) TABLESPACE IKROME_DATA STORAGE ( INITIAL 256K NEXT 256K );
```
</kodo>

Now that I had tables capable of holding larger data sizes it was time to test the Data Migration again.  Right away we run into a problem.  The Data Migration doesn't copy data if the table already exists in the target database.  The root of the problem lies in the [com.liferay.portal.convert.ConvertDatabase](https://docs.liferay.com/portal/6.2/javadocs-all/src-html/com/liferay/portal/convert/ConvertDatabase.html#line.271) classs' migrateTable method (shown below).  

<kodo apudskribo="Original migrateTable method">

```
            protected void migrateTable(
                            DB db, Connection connection, String tableName, Object[][] columns,
                            String sqlCreate)
                    throws Exception {
    
                    Table table = new Table(tableName, columns);
    
                    try {
                            String tempFileName = table.generateTempFile();
    
                            db.runSQL(connection, sqlCreate);
    
                            if (tempFileName != null) {
                                    table.populateTable(tempFileName, connection);
                            }
                    }
                    catch (Exception e) {
                            _log.error(e, e);
    
                            MaintenanceUtil.appendStatus(e.getMessage());
                    }
            }
```
</kodo>

As can be seen if the SQL to create a table throws an exception then the table will not be populated.  My solution 
was to copy and modify the ConvertDatabase class.  My new class, call ModifiedConvertDatabase, was modified to the following.

<span class="codeCaption">modified migrate table method</span>
<kodo apudskribo="modified migrate table method">

```
protected void migrateTable(DB db, Connection connection, String tableName, Object[][] columns, String sqlCreate)
      throws Exception {

    Table table = new Table(tableName, columns);
    String tempFileName = table.generateTempFile();

    try {
      if(!hasTable(db, connection, tableName, columns, sqlCreate)) {
        db.runSQL(connection, sqlCreate);
      }
      if (tempFileName != null) {
        table.populateTable(tempFileName, connection);
      }

    } catch (Exception e) {
      _log.error(e, e);

      MaintenanceUtil.appendStatus(e.getMessage());
    }
    
  }

  protected boolean hasTable(DB db, Connection connection, String tableName, Object[][] columns, String sqlCreate)
      throws Exception {

    boolean returnValue = false;
    Table table = new Table(tableName, columns);

    try {
      String tempFileName = table.generateTempFile();

      db.runSQL(connection, "select * from " + tableName);
      returnValue = true;
    } catch (Exception e) {
      _log.warn("Table " + tableName + "doesn't exist");
    }
    return returnValue;
  }
```
</kodo>

In this modified code the table creation SQL is only run if the table doesn't already exist.  This way if the table doesn't exist
it is created and populated.  If it does exist it is only populated.  Now we need to configure Liferay to use our new class.  Add or update the [convert.processes](https://docs.liferay.com/portal/6.2/propertiesdoc/portal.properties.html) to the following in portal-ext.properties.

<kodo apudskribo="portal-ext.properties">

```
convert.processes=com.example.ModifiedConvertDatabase,\
  com.liferay.portal.convert.ConvertDocumentLibrary,\
  com.liferay.portal.convert.ConvertDocumentLibraryExtraSettings,\
  com.liferay.portal.convert.ConvertWikiCreole
```
</kodo>

With these modifications in place I had my first successful Data migration.  How would I know if all the data transferred
correctly?  My first step was to create a SQL script that counted tables rows for all the tables.  I could run this against
both Oracle and Sybase.  An excerpt is below.

<kodo apudskribo="row count union">

```
SELECT 'ACCOUNT_', count(*) FROM ACCOUNT_
UNION
SELECT 'ADDRESS', count(*) FROM ADDRESS
UNION
SELECT 'ANNOUNCEMENTSDELIVERY', count(*) FROM ANNOUNCEMENTSDELIVERY
UNION
SELECT 'ANNOUNCEMENTSENTRY', count(*) FROM ANNOUNCEMENTSENTRY
UNION
SELECT 'ANNOUNCEMENTSFLAG', count(*) FROM ANNOUNCEMENTSFLAG
```
</kodo>

Using a diff tool I compared the results and found that they matched.  Whew!  Looking pretty good so far.
I know my row counts are good but what about my data?  Did those large clob columns transfer correctly?
I ran SQL for max, min, avg against the Oracle tables and compared it to the results from the Sybase Tables.
Fortunately those numbers also matched up!  Finally we are done.
<!--
*  Oracle table definitions
*  Failed to copy data if table exists in target database.
*  Modified ConvertDatabase class to copy data if the class exists.
*  If you are accessing an external database using the liferay service layer.
*  Created SQL Script to create empty tables for tables used in external database.
*  Upgraded the Notifications plugin to shorten table name for Oracle.
*  Custom portlets have table names too long.
*  Oracle CLOB columns defaulted to 4k.
*  Wrote Java program to find sizes of text columns in sybase.
*  
-->
