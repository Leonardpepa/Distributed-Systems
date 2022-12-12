import sys
import mariadb
from src.bank.model.statement import Statement

def create(cursor, stmt):
    try:
        cursor.execute("INSERT INTO statement (account_id, type, message) VALUES (?, ?, ?)", [stmt.account_id, stmt.type, stmt.message])
        return cursor.rowcount >= 1
    
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False


def read(cursor, id):
    try:
        cursor.execute("SELECT * FROM statement WHERE id = ?",  [id])
        result = cursor.fetchone()
    
        if result is None:
            return False, None
    
        result["timestamp"] = str(result["timestamp"])
    
        return True, result
    
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None
    
def read_by_acc_id(cursor, acc_id):
    try:
        cursor.execute("SELECT * FROM statement WHERE account_id = ?",  [acc_id])
        result = cursor.fetchall()
        
        if result is None:
            return False, None
        
        for res in result:
            res["timestamp"] = str(res["timestamp"])
            
        return True, result
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None