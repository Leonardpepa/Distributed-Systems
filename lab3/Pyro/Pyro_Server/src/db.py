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
        return conn, conn.cursor()
    except mariadb.Error as e:
        print(f"Error connecting to MariaDB Platform: {e}")
        sys.exit(1)


def auth(cursor, id: int, pin: int):
    try:
        cursor.execute("SELECT * FROM account WHERE id=? AND pin=?", [id, pin])
        return cursor.fetchone()
    except mariadb.Error as e:
        print(f"Error: {e}")

def create(cursor, acc: Account):
    try:
        cursor.execute("INSERT INTO account (id,pin,name,balance) VALUES (?, ?, ?, ?)", [acc.id, acc.pin, acc.name, 0])
        return cursor.rowcount >= 1
    except mariadb.Error as e:
        print(f"Error: {e}")

def read(cursor, id: int):
    try:
        cursor.execute("SELECT * FROM account WHERE id=?",  [id])
        return cursor.fetchone()
    except mariadb.Error as e:
        print(f"Error: {e}")

def update(cursor, acc: Account):
    try:
        cursor.execute("UPDATE account SET pin=?, name=?, balance=? WHERE id=?", [acc.pin, acc.name, acc.balance, acc.id])
        if cursor.rowcount >= 1:
            return read(cursor=cursor, id=acc.id)
        else:
            return None
    except mariadb.Error as e:
        print(f"Error: {e}")

