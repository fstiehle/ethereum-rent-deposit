package com.example.RESTapi;

import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.Event;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Oracle {

    private final String contractAddress = "0x0C1bCcaa3272e319C0F4275EDB24c68EFEA84F45";
    private final int ATTEMPTS = 12;

    private final Web3j web3;
    private final Credentials wallet;
    private final static Logger LOGGER = Logger.getLogger(Oracle.class.getName());

    public Oracle() {
        this.web3 = Web3j.build(new HttpService());
        this.wallet = Credentials.create("0x87e6d2b12bf25338c0ffd05868b5a22c4583ceaa39033fac756a7565a8b42f5b");
    }

    /**
     * Call Factory contract &
     * Confirm it is mined and get Emitted event
     * @param property
     * @return String address of created contract
     */
    public String createRentContract(Property property) {
        String transactionHash = this.sendTransaction(property);
        TransactionReceipt receipt = this.confirmTransaction(transactionHash);

        if (null != receipt) {
            Event event = new Event("ContractCreated",
                Arrays.asList(new TypeReference<Int>() {}, new TypeReference<Address>() {}));

            EventValues values = Contract.staticExtractEventParameters(event, receipt.getLogs().get(0));
            if (null == values || 0 == values.getNonIndexedValues().size()) {
                LOGGER.severe("Contract didn't emit ContractCreated Event");
                return null;
            }

            String result = values.getNonIndexedValues().get(1).toString();
            LOGGER.info("Rent contract created at: " + result);
            return result;
        }

        return null;
    }

    /**
     * Confirm that transaction is mined
     * @param transactionHash
     * @return TransactionReceipt trasnactionReceipt
     */
    private TransactionReceipt confirmTransaction(String transactionHash) {
        Optional<TransactionReceipt> receipt;

        EthGetTransactionReceipt answer;
        try {
            answer = web3.ethGetTransactionReceipt(transactionHash).send();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }

        receipt = answer.getTransactionReceipt();
        for (int i = 0; i < ATTEMPTS; i++) {

            if (receipt.isPresent()) {
                return receipt.get();
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                return null;
            }
        }

        return null;
    }

    /**
     * Call factory contract
     * @param property
     * @return String transactionHash
     */
    private String sendTransaction(Property property) {
        Function function = new Function(
                "createInstance",
                Arrays.asList(
                        new Address(property.getOwnerPublicKey()),
                        new Address(property.getTenantPublicKey()),
                        new Uint(BigInteger.valueOf(property.getDepositAmount())),
                        new Uint(BigInteger.valueOf(property.getStartDate().getTime() / 1000)),
                        new Uint(BigInteger.valueOf(property.getEndDate().getTime() / 1000)),
                        new Int(BigInteger.valueOf(property.getHashValue()))),
                Arrays.asList(new TypeReference<Address>() {} ));

        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger nonce = this.getNonce();


        Transaction transaction = Transaction.createFunctionCallTransaction(
                this.wallet.getAddress(),
                nonce,
                Contract.GAS_PRICE,
                Contract.GAS_LIMIT,
                this.contractAddress,
                BigInteger.ZERO,
                encodedFunction);

        EthSendTransaction response;
        try {
            response = web3.ethSendTransaction(transaction).sendAsync().get();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }

        if (response.hasError()) {
            LOGGER.severe(response.getError().getMessage());
            return null;
        }

        return response.getTransactionHash();
    }

    private BigInteger getNonce() {
        EthGetTransactionCount ethGetTransactionCount;
        try {
            ethGetTransactionCount = this.web3.ethGetTransactionCount(
                    this.wallet.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }

        return ethGetTransactionCount.getTransactionCount();
    }

}
