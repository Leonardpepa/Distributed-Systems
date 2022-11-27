// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bank.proto

package org.bank.grpc.Bank;

/**
 * Protobuf type {@code org.bank.grpc.Bank.TransferRequest}
 */
public final class TransferRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:org.bank.grpc.Bank.TransferRequest)
    TransferRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TransferRequest.newBuilder() to construct.
  private TransferRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TransferRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new TransferRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.bank.grpc.Bank.Bank.internal_static_org_bank_grpc_Bank_TransferRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.bank.grpc.Bank.Bank.internal_static_org_bank_grpc_Bank_TransferRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.bank.grpc.Bank.TransferRequest.class, org.bank.grpc.Bank.TransferRequest.Builder.class);
  }

  public static final int ID_FROM_FIELD_NUMBER = 1;
  private int idFrom_;
  /**
   * <code>int32 id_from = 1;</code>
   * @return The idFrom.
   */
  @java.lang.Override
  public int getIdFrom() {
    return idFrom_;
  }

  public static final int ID_TO_FIELD_NUMBER = 2;
  private int idTo_;
  /**
   * <code>int32 id_to = 2;</code>
   * @return The idTo.
   */
  @java.lang.Override
  public int getIdTo() {
    return idTo_;
  }

  public static final int AMOUNT_FIELD_NUMBER = 3;
  private float amount_;
  /**
   * <code>float amount = 3;</code>
   * @return The amount.
   */
  @java.lang.Override
  public float getAmount() {
    return amount_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (idFrom_ != 0) {
      output.writeInt32(1, idFrom_);
    }
    if (idTo_ != 0) {
      output.writeInt32(2, idTo_);
    }
    if (java.lang.Float.floatToRawIntBits(amount_) != 0) {
      output.writeFloat(3, amount_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (idFrom_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, idFrom_);
    }
    if (idTo_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, idTo_);
    }
    if (java.lang.Float.floatToRawIntBits(amount_) != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(3, amount_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.bank.grpc.Bank.TransferRequest)) {
      return super.equals(obj);
    }
    org.bank.grpc.Bank.TransferRequest other = (org.bank.grpc.Bank.TransferRequest) obj;

    if (getIdFrom()
        != other.getIdFrom()) return false;
    if (getIdTo()
        != other.getIdTo()) return false;
    if (java.lang.Float.floatToIntBits(getAmount())
        != java.lang.Float.floatToIntBits(
            other.getAmount())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FROM_FIELD_NUMBER;
    hash = (53 * hash) + getIdFrom();
    hash = (37 * hash) + ID_TO_FIELD_NUMBER;
    hash = (53 * hash) + getIdTo();
    hash = (37 * hash) + AMOUNT_FIELD_NUMBER;
    hash = (53 * hash) + java.lang.Float.floatToIntBits(
        getAmount());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bank.grpc.Bank.TransferRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.bank.grpc.Bank.TransferRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bank.grpc.Bank.TransferRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.bank.grpc.Bank.TransferRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code org.bank.grpc.Bank.TransferRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.bank.grpc.Bank.TransferRequest)
      org.bank.grpc.Bank.TransferRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.bank.grpc.Bank.Bank.internal_static_org_bank_grpc_Bank_TransferRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.bank.grpc.Bank.Bank.internal_static_org_bank_grpc_Bank_TransferRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.bank.grpc.Bank.TransferRequest.class, org.bank.grpc.Bank.TransferRequest.Builder.class);
    }

    // Construct using org.bank.grpc.Bank.TransferRequest.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      idFrom_ = 0;

      idTo_ = 0;

      amount_ = 0F;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.bank.grpc.Bank.Bank.internal_static_org_bank_grpc_Bank_TransferRequest_descriptor;
    }

    @java.lang.Override
    public org.bank.grpc.Bank.TransferRequest getDefaultInstanceForType() {
      return org.bank.grpc.Bank.TransferRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.bank.grpc.Bank.TransferRequest build() {
      org.bank.grpc.Bank.TransferRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.bank.grpc.Bank.TransferRequest buildPartial() {
      org.bank.grpc.Bank.TransferRequest result = new org.bank.grpc.Bank.TransferRequest(this);
      result.idFrom_ = idFrom_;
      result.idTo_ = idTo_;
      result.amount_ = amount_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.bank.grpc.Bank.TransferRequest) {
        return mergeFrom((org.bank.grpc.Bank.TransferRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.bank.grpc.Bank.TransferRequest other) {
      if (other == org.bank.grpc.Bank.TransferRequest.getDefaultInstance()) return this;
      if (other.getIdFrom() != 0) {
        setIdFrom(other.getIdFrom());
      }
      if (other.getIdTo() != 0) {
        setIdTo(other.getIdTo());
      }
      if (other.getAmount() != 0F) {
        setAmount(other.getAmount());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              idFrom_ = input.readInt32();

              break;
            } // case 8
            case 16: {
              idTo_ = input.readInt32();

              break;
            } // case 16
            case 29: {
              amount_ = input.readFloat();

              break;
            } // case 29
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }

    private int idFrom_ ;
    /**
     * <code>int32 id_from = 1;</code>
     * @return The idFrom.
     */
    @java.lang.Override
    public int getIdFrom() {
      return idFrom_;
    }
    /**
     * <code>int32 id_from = 1;</code>
     * @param value The idFrom to set.
     * @return This builder for chaining.
     */
    public Builder setIdFrom(int value) {
      
      idFrom_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 id_from = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearIdFrom() {
      
      idFrom_ = 0;
      onChanged();
      return this;
    }

    private int idTo_ ;
    /**
     * <code>int32 id_to = 2;</code>
     * @return The idTo.
     */
    @java.lang.Override
    public int getIdTo() {
      return idTo_;
    }
    /**
     * <code>int32 id_to = 2;</code>
     * @param value The idTo to set.
     * @return This builder for chaining.
     */
    public Builder setIdTo(int value) {
      
      idTo_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 id_to = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearIdTo() {
      
      idTo_ = 0;
      onChanged();
      return this;
    }

    private float amount_ ;
    /**
     * <code>float amount = 3;</code>
     * @return The amount.
     */
    @java.lang.Override
    public float getAmount() {
      return amount_;
    }
    /**
     * <code>float amount = 3;</code>
     * @param value The amount to set.
     * @return This builder for chaining.
     */
    public Builder setAmount(float value) {
      
      amount_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>float amount = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearAmount() {
      
      amount_ = 0F;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:org.bank.grpc.Bank.TransferRequest)
  }

  // @@protoc_insertion_point(class_scope:org.bank.grpc.Bank.TransferRequest)
  private static final org.bank.grpc.Bank.TransferRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.bank.grpc.Bank.TransferRequest();
  }

  public static org.bank.grpc.Bank.TransferRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TransferRequest>
      PARSER = new com.google.protobuf.AbstractParser<TransferRequest>() {
    @java.lang.Override
    public TransferRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<TransferRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TransferRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.bank.grpc.Bank.TransferRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

