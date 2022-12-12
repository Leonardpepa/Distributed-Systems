import mariadb
from src.bank.model.account import Account
import datetime
import src.bank.model.db as db

def get_all_accounts():
    try:
        conn, cursor = db.db_connection()
        cursor.execute("SELECT `id` FROM `account` WHERE 1 ")
        result = cursor.fetchall()
        cursor.close()
        conn.close()
        id_list = [int(el["id"]) for el in result]   
        return id_list
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None
        
    
def auth(cursor, id, pin):
    try:
        cursor.execute("SELECT `id`, `name`, `date` FROM account WHERE `id`=? AND `pin`=?", [id, pin])
        result = cursor.fetchone()
        if result is None:
            return False, None
        
        result["date"] = str(result["date"])
        return True, result
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None

def create(cursor, acc):
    try:
        cursor.execute("INSERT INTO `account`(`id`, `pin`, `name`, `balance`, `limit`) VALUES (?,?,?,?,?)", [acc.id, acc.pin, acc.name, 0, acc.limit])
        return cursor.rowcount >= 1
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False


def read(cursor, id):
    try:
        cursor.execute("SELECT `id`, `name`, `balance`, `limit`, `date` FROM `account` WHERE `id`=? ",  [id])
        result = cursor.fetchone()
        if result is None:
            return False, None
        result["date"] = str(result["date"])
        return True, result
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None

def update(cursor, acc):
    try:
        cursor.execute("UPDATE account SET `name`=?, `balance`=?, `limit`=? WHERE `id`=?", [acc.name, acc.balance, acc.limit, acc.id])
        if cursor.rowcount >= 1:
            return read(cursor=cursor, id=acc.id)
        else:
            return False, None
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None
        

def update_daily_limit(cursor, id, limit):
    try:
        cursor.execute("UPDATE account SET `limit`=?, `date`=? WHERE `id`=?", [limit, datetime.date.today() ,id])
        if cursor.rowcount >= 1:
            return read(cursor=cursor, id=id)
        else:
            return False, None
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None
