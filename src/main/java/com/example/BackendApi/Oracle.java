package com.example.BackendApi;

import io.github.cdimascio.dotenv.Dotenv;
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
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Oracle {

    private final String contractAddress;

    private final Web3j web3;
    private final Credentials wallet;
    private final static Logger LOGGER = Logger.getLogger(Oracle.class.getName());

    public Oracle() {
        Dotenv dotenv = Dotenv.load();
        this.web3 = Web3j.build(new HttpService(dotenv.get("BLOCKCHAIN_NODE_URL")));
        this.wallet = Credentials.create(dotenv.get("PRIVATE_KEY"));
        this.contractAddress = dotenv.get("CONTRACT_ADDRESS");
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

            if (0 == receipt.getLogs().size()) {
                LOGGER.severe("Contract didn't emit ContractCreated Event");
                return null;
            }

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

        LOGGER.severe("Connection to blockchain network failed");
        return null;
    }

    /**
     * Confirm that transaction is mined
     * @param transactionHash
     * @return TransactionReceipt trasnactionReceipt
     */
    private TransactionReceipt confirmTransaction(String transactionHash) {

        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(
                this.web3,
                TransactionManager.DEFAULT_POLLING_FREQUENCY,
                TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

        TransactionReceipt receipt;
        try {
            receipt = processor.waitForTransactionReceipt(transactionHash);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } catch (TransactionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }

        return receipt;
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

        TransactionManager transactionManager = new RawTransactionManager(this.web3, this.wallet);

        EthSendTransaction response;
        try {
            response = transactionManager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    this.contractAddress,
                    encodedFunction,
                    BigInteger.ZERO);

        } catch (IOException e) {
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