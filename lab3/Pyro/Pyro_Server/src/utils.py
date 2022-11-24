from datetime import datetime
def day_passed(current, db_date) -> int:
    try:
        current = str(current).replace('-', '/')
        db_date = str(db_date).replace('-', '/')
        curr = datetime.strptime(str(current), "%Y/%m/%d")
        registered = datetime.strptime(str(db_date), "%Y/%m/%d")
        delta = curr - registered
        return delta.days
    except ValueError as error:
        print(f"Error: {error}")