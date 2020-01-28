package com.example.ECProject;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Oracle {

    private final String contractAddress = "0xF9AD9835a2eF648186d18B6303824f7A500Ca9Ad";
    private final int CONFIRMATIONBLOCKS = 12;

    private final Web3j web3;
    private final Credentials wallet;

    public Oracle() {
        this.web3 = Web3j.build(new HttpService());
        this.wallet = Credentials.create("0x87e6d2b12bf25338c0ffd05868b5a22c4583ceaa39033fac756a7565a8b42f5b");
    }

    /**
     *
     * @param property
     * @return String address of created contract
     */
    public String createRentContract(Property property) {
        String transactionHash = this.sendTransaction(property);
        return "test";
    }

    private String confirmTransaction(String transactionHash) {
        return "test";
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
                        new Address("0x1A218f5BA9C14f594ECBf9DD07F5B9E0885114a1"),
                        new Address("0xA68F9485d4be42A413D4dbe7EcEb180784986967"),
                        new Uint(BigInteger.ONE),
                        new Uint(BigInteger.ONE),
                        new Uint(BigInteger.TEN),
                        new Uint(BigInteger.ZERO)),
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
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        if (response.hasError()) {
            System.out.println(response.getError().getMessage());
            return null;
        }

        return response.getTransactionHash();
    }

    private BigInteger getNonce() {
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = this.web3.ethGetTransactionCount(
                    this.wallet.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return ethGetTransactionCount.getTransactionCount();
    }

}
