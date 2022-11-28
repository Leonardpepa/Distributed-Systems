from google.protobuf.internal import enum_type_wrapper as _enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional, Union as _Union

ADD: Operator
DESCRIPTOR: _descriptor.FileDescriptor
DIV: Operator
MUL: Operator
SUB: Operator

class BinaryOpRequest(_message.Message):
    __slots__ = ["operandA", "operandB", "operator"]
    OPERANDA_FIELD_NUMBER: _ClassVar[int]
    OPERANDB_FIELD_NUMBER: _ClassVar[int]
    OPERATOR_FIELD_NUMBER: _ClassVar[int]
    operandA: float
    operandB: float
    operator: Operator
    def __init__(self, operator: _Optional[_Union[Operator, str]] = ..., operandA: _Optional[float] = ..., operandB: _Optional[float] = ...) -> None: ...

class BinaryOpResponse(_message.Message):
    __slots__ = ["result"]
    RESULT_FIELD_NUMBER: _ClassVar[int]
    result: float
    def __init__(self, result: _Optional[float] = ...) -> None: ...

class Operator(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = []
