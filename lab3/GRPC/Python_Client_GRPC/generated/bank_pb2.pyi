from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class Account(_message.Message):
    __slots__ = ["balance", "date", "id", "limit", "name", "pin"]
    BALANCE_FIELD_NUMBER: _ClassVar[int]
    DATE_FIELD_NUMBER: _ClassVar[int]
    ID_FIELD_NUMBER: _ClassVar[int]
    LIMIT_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    PIN_FIELD_NUMBER: _ClassVar[int]
    balance: float
    date: str
    id: int
    limit: float
    name: str
    pin: int
    def __init__(self, id: _Optional[int] = ..., pin: _Optional[int] = ..., name: _Optional[str] = ..., balance: _Optional[float] = ..., limit: _Optional[float] = ..., date: _Optional[str] = ...) -> None: ...

class AuthRequest(_message.Message):
    __slots__ = ["id", "pin"]
    ID_FIELD_NUMBER: _ClassVar[int]
    PIN_FIELD_NUMBER: _ClassVar[int]
    id: int
    pin: int
    def __init__(self, id: _Optional[int] = ..., pin: _Optional[int] = ...) -> None: ...

class BalanceResponse(_message.Message):
    __slots__ = ["balance", "ok"]
    BALANCE_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    balance: float
    ok: Ok
    def __init__(self, balance: _Optional[float] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class DepositRequest(_message.Message):
    __slots__ = ["amount", "id"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    ID_FIELD_NUMBER: _ClassVar[int]
    amount: float
    id: int
    def __init__(self, id: _Optional[int] = ..., amount: _Optional[float] = ...) -> None: ...

class DepositResponse(_message.Message):
    __slots__ = ["balance", "ok"]
    BALANCE_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    balance: float
    ok: Ok
    def __init__(self, balance: _Optional[float] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class Empty(_message.Message):
    __slots__ = []
    def __init__(self) -> None: ...

class GenericRequest(_message.Message):
    __slots__ = ["id"]
    ID_FIELD_NUMBER: _ClassVar[int]
    id: int
    def __init__(self, id: _Optional[int] = ...) -> None: ...

class InfoResponse(_message.Message):
    __slots__ = ["account", "ok"]
    ACCOUNT_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    account: Account
    ok: Ok
    def __init__(self, account: _Optional[_Union[Account, _Mapping]] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class LoginResponse(_message.Message):
    __slots__ = ["id", "name", "ok"]
    ID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    id: int
    name: str
    ok: Ok
    def __init__(self, id: _Optional[int] = ..., name: _Optional[str] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class Ok(_message.Message):
    __slots__ = ["message", "ok"]
    MESSAGE_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    message: str
    ok: bool
    def __init__(self, ok: bool = ..., message: _Optional[str] = ...) -> None: ...

class RegisterRequest(_message.Message):
    __slots__ = ["id", "name", "pin"]
    ID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    PIN_FIELD_NUMBER: _ClassVar[int]
    id: int
    name: str
    pin: int
    def __init__(self, id: _Optional[int] = ..., pin: _Optional[int] = ..., name: _Optional[str] = ...) -> None: ...

class RegisterResponse(_message.Message):
    __slots__ = ["id", "name", "ok"]
    ID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    id: int
    name: str
    ok: Ok
    def __init__(self, id: _Optional[int] = ..., name: _Optional[str] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class Statement(_message.Message):
    __slots__ = ["accountId", "date", "message", "type"]
    ACCOUNTID_FIELD_NUMBER: _ClassVar[int]
    DATE_FIELD_NUMBER: _ClassVar[int]
    MESSAGE_FIELD_NUMBER: _ClassVar[int]
    TYPE_FIELD_NUMBER: _ClassVar[int]
    accountId: int
    date: str
    message: str
    type: str
    def __init__(self, accountId: _Optional[int] = ..., type: _Optional[str] = ..., message: _Optional[str] = ..., date: _Optional[str] = ...) -> None: ...

class StatementResponse(_message.Message):
    __slots__ = ["ok", "statement"]
    OK_FIELD_NUMBER: _ClassVar[int]
    STATEMENT_FIELD_NUMBER: _ClassVar[int]
    ok: Ok
    statement: _containers.RepeatedCompositeFieldContainer[Statement]
    def __init__(self, statement: _Optional[_Iterable[_Union[Statement, _Mapping]]] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class TransferRequest(_message.Message):
    __slots__ = ["amount", "id_from", "id_to", "name_to"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    ID_FROM_FIELD_NUMBER: _ClassVar[int]
    ID_TO_FIELD_NUMBER: _ClassVar[int]
    NAME_TO_FIELD_NUMBER: _ClassVar[int]
    amount: float
    id_from: int
    id_to: int
    name_to: str
    def __init__(self, id_from: _Optional[int] = ..., id_to: _Optional[int] = ..., name_to: _Optional[str] = ..., amount: _Optional[float] = ...) -> None: ...

class TransferResponse(_message.Message):
    __slots__ = ["amount", "ok"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    amount: float
    ok: Ok
    def __init__(self, amount: _Optional[float] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...

class WithdrawRequest(_message.Message):
    __slots__ = ["amount", "id"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    ID_FIELD_NUMBER: _ClassVar[int]
    amount: float
    id: int
    def __init__(self, id: _Optional[int] = ..., amount: _Optional[float] = ...) -> None: ...

class WithdrawResponse(_message.Message):
    __slots__ = ["balance", "ok"]
    BALANCE_FIELD_NUMBER: _ClassVar[int]
    OK_FIELD_NUMBER: _ClassVar[int]
    balance: float
    ok: Ok
    def __init__(self, balance: _Optional[float] = ..., ok: _Optional[_Union[Ok, _Mapping]] = ...) -> None: ...
