type=tip
title=Grep file return unique values
date=2018-05-07
tags=linux, grep, sort, unique
status=published
category=Tech Tip
~~~~~~
Grep file return unique values
<!--summary marker-->
<finastacio apudskribo="Command Line">
```
grep -oh "^[0-9,\.]*[ ]" localhost_access_log.2018-05-07.txt | sort -u
```
</finastacio>
