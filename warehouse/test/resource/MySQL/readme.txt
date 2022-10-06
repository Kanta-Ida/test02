【MYSQLの起動】
> cd C:\training\mysql-5.7.17-win32
> "bin/mysqld" --defaults-file="C:\training\mysql-5.7.17-win32\my.ini" --console

【MYSQLの停止】
> cd C:\training\mysql-5.7.17-win32
> "bin/mysqladmin" shutdown -u root -p

【MYSQLの利用】
> "bin/mysql" -u root
> use warehouse;
> ・・・
> quit

【注意】
ビルドパスをHSQLDBからMySQLのjarを参照するように切り替えてください。