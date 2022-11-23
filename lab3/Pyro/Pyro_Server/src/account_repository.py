import mariadb
from account import Account
        
    
def auth(cursor, id: int, pin: int) -> tuple[bool, any]:
    try:
        cursor.execute("SELECT id, name FROM account WHERE id=? AND pin=?", [id, pin])
        result = cursor.fetchone()
        if result is None:
            return False, None
        return True, result
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None

def create(cursor, acc: Account) -> bool:
    try:
        cursor.execute("INSERT INTO account (id,pin,name,balance) VALUES (?, ?, ?, ?)", [acc.id, acc.pin, acc.name, 0])
        return cursor.rowcount >= 1
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False


def read(cursor, id: int):
    try:
        cursor.execute("SELECT id, name, balance FROM account WHERE id=?",  [id])
        result = cursor.fetchone()
        if result is None:
            return False, None
        return True, result
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None

def update(cursor, acc: Account):
    try:
        cursor.execute("UPDATE account SET name=?, balance=? WHERE id=?", [acc.name, acc.balance, acc.id])
        if cursor.rowcount >= 1:
            return read(cursor=cursor, id=acc.id)
        else:
            return False, None
    except mariadb.Error as e:
        print(f"Error: {e}")
        return False, None
        

