// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bank.proto

package org.bank.grpc.Bank;

public interface StatementResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.bank.grpc.Bank.StatementResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.org.bank.grpc.Bank.Statement statement = 1;</code>
   * @return Whether the statement field is set.
   */
  boolean hasStatement();
  /**
   * <code>.org.bank.grpc.Bank.Statement statement = 1;</code>
   * @return The statement.
   */
  org.bank.grpc.Bank.Statement getStatement();
  /**
   * <code>.org.bank.grpc.Bank.Statement statement = 1;</code>
   */
  org.bank.grpc.Bank.StatementOrBuilder getStatementOrBuilder();

  /**
   * <code>.org.bank.grpc.Bank.Ok ok = 2;</code>
   * @return Whether the ok field is set.
   */
  boolean hasOk();
  /**
   * <code>.org.bank.grpc.Bank.Ok ok = 2;</code>
   * @return The ok.
   */
  org.bank.grpc.Bank.Ok getOk();
  /**
   * <code>.org.bank.grpc.Bank.Ok ok = 2;</code>
   */
  org.bank.grpc.Bank.OkOrBuilder getOkOrBuilder();
}
