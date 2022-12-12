import sys
import mariadb
from src.bank.model.account import Account

def db_connection():
    # Connect to MariaDB Platform
    try:
        conn = mariadb.connect(
            user="root",
            password="",
            host="localhost",
            port=3306,
            database="bank",
            autocommit=True
        )
        return conn, conn.cursor(dictionary=True)
    except mariadb.Error as e:
        print(f"Error connecting to MariaDB Platform: {e}")
        sys.exit(1)
        
def initialize_db():
    try:
        
        conn = mariadb.connect(
            user="root",
            password="",
            host="localhost",
            port=3306,
            database="",
            autocommit=True
        )
        cur = conn.cursor(dictionary=True)
        
        sql = "CREATE DATABASE IF NOT EXISTS bank;"
        cur.execute(sql)
        
        conn, cur = db_connection()
        
        sql = "CREATE TABLE IF NOT EXISTS `statement` (`id` INT NOT NULL AUTO_INCREMENT , `account_id` INT NOT NULL , `type` VARCHAR(100) NOT NULL , `message` VARCHAR(255) NOT NULL , `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`));"
        cur.execute(sql)
        sql = "CREATE TABLE IF NOT EXISTS `account` (`id` INT NOT NULL , `pin` INT NOT NULL , `name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL , `limit` DOUBLE NOT NULL , `date` DATE NOT NULL DEFAULT CURRENT_TIMESTAMP );"
        cur.execute(sql)
        cur.close()
        conn.close()
    except mariadb.Error as e:
        print(f"Error: {e}")
