// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bank.proto

package org.bank.grpc.Bank;

public interface TransferRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.bank.grpc.Bank.TransferRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 id_from = 1;</code>
   * @return The idFrom.
   */
  int getIdFrom();

  /**
   * <code>int32 id_to = 2;</code>
   * @return The idTo.
   */
  int getIdTo();

  /**
   * <code>float amount = 3;</code>
   * @return The amount.
   */
  float getAmount();
}
