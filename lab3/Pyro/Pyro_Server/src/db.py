import sys
import mariadb
from account import Account

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
        sql = "CREATE TABLE IF NOT EXISTS `bank`.`statement` (`id` INT NOT NULL AUTO_INCREMENT , `account_id` INT NOT NULL , `type` VARCHAR(100) NOT NULL , `message` VARCHAR(255) NOT NULL , `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`));"
        conn, cur = db_connection()
        cur.execute(sql)
        cur.close()
        conn.close()
    except mariadb.Error as e:
        print(f"Error: {e}")
