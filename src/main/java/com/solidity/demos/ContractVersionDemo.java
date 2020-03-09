package com.solidity.demos;

import com.citahub.cita.abi.FunctionEncoder;
import com.citahub.cita.abi.FunctionReturnDecoder;
import com.citahub.cita.abi.TypeReference;
import com.citahub.cita.abi.datatypes.Address;
import com.citahub.cita.abi.datatypes.Function;
import com.citahub.cita.abi.datatypes.Type;
import com.citahub.cita.abi.datatypes.Uint;
import com.citahub.cita.abi.datatypes.generated.Uint256;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.DefaultBlockParameter;
import com.citahub.cita.protocol.core.DefaultBlockParameterName;
import com.citahub.cita.protocol.core.methods.request.Call;
import com.citahub.cita.protocol.core.methods.request.Transaction;
import com.citahub.cita.protocol.core.methods.request.Transaction.CryptoTx;
import com.citahub.cita.protocol.core.methods.response.AppGetTransactionReceipt;
import com.citahub.cita.protocol.core.methods.response.AppMetaData;
import com.citahub.cita.protocol.core.methods.response.AppSendTransaction;
import com.citahub.cita.protocol.core.methods.response.TransactionReceipt;
import com.citahub.cita.protocol.http.HttpService;
import com.citahub.cita.protocol.system.CITASystemContract;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ContractVersionDemo {

  // CITA 测试链
  public static String url = "https://testnet.citahub.com";
  public static CITAj service = CITAj.build(new HttpService(url));
  public static CryptoTx cryptoTx = Transaction.CryptoTx.valueOf("SECP256K1");

  public static int version;
  public static BigInteger chainId;

  // Test Account
  public static String adminPrivateKey = "0x5f0258a4778057a8a7d97809bd209055b2fbafa654ce7d31ec7191066b9225e6";
  public static String adminPublicAddress = "0x4b5ae4567ad5d9fb92bc9afd6a657e6fa13a2523";

  // source sol: src/main/resources/ControlVersion.sol
  public static String ContractVersionCode = "0x608060405234801561001057600080fd5b5060008060020181905550610f668061002a6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630ee70ebc14610088578063433c17d3146100e957806346666ac0146101445780635f26a1bc146101c957806388cf0e2714610261578063a080b5c7146102c6578063f61489fb14610353575b600080fd5b34801561009457600080fd5b506100d3600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506103ae565b6040518082815260200191505060405180910390f35b3480156100f557600080fd5b5061012a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061055a565b604051808215151515815260200191505060405180910390f35b34801561015057600080fd5b506101af600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610690565b604051808215151515815260200191505060405180910390f35b3480156101d557600080fd5b5061020a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610ab5565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561024d578082015181840152602081019050610232565b505050509050019250505060405180910390f35b34801561026d57600080fd5b506102ac600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610c92565b604051808215151515815260200191505060405180910390f35b3480156102d257600080fd5b50610311600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610cc0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561035f57600080fd5b50610394600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610d6c565b604051808215151515815260200191505060405180910390f35b600080606060006103be86610d6c565b9250821561052d576000800160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805480602002602001604051908101604052809291908181526020016000905b828210156104b95783829060005260206000209060020201604080519081016040529081600082015481526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152505081526020019060010190610427565b505050509150600090505b8151811015610505578482828151811015156104dc57fe5b906020019060200201516000015114156104f857809350610551565b80806001019150506104c4565b7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9350610551565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe93505b50505092915050565b60008060008091505b6000600101805490508210156106845760006001018281548110151561058557fe5b9060005260206000200160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610677576000800160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006106369190610dcd565b600160006001018381548110151561064a57fe5b9060005260206000200160000160146101000a81548160ff02191690831515021790555060019250610689565b8180600101925050610563565b600092505b5050919050565b600080600061069d610df1565b60006106a988886103ae565b9350868360000181905550858360010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe8414156108f55787826000019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505060008260200190151590811515815250508281600081548110151561077857fe5b9060005260206000209060020201600082015481600001556001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550905050806000800160008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090805461084a929190610e23565b5081600060010160006002015481548110151561086357fe5b9060005260206000200160008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555090505060006002016000815480929190600101919050555060009450610aaa565b7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff841415610a07576000800160008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208390806001815401808255809150509060018203906000526020600020906002020160009091929091909150600082015481600001556001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050505060009450610aaa565b856000800160008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002085815481101515610a5657fe5b906000526020600020906002020160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600194505b505050509392505050565b60606000806000610ac585610d6c565b92508215610c0257600090505b6000800160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080549050811015610c01576000800160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081815481101515610b6c57fe5b906000526020600020906002020160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168282815481101515610bac57fe5b9060005260206000200160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508080600101915050610ad2565b5b81805480602002602001604051908101604052809291908181526020018280548015610c8357602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610c39575b50505050509350505050919050565b600080610c9f84846103ae565b9050600081121515610cb45760019150610cb9565b600091505b5092915050565b600080610ccd84846103ae565b9050600081121515610d60576000800160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081815481101515610d2757fe5b906000526020600020906002020160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169150610d65565b600091505b5092915050565b6000806000800160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805490501115610dc35760019050610dc8565b600090505b919050565b5080546000825560020290600052602060002090810190610dee9190610eec565b50565b6040805190810160405280600073ffffffffffffffffffffffffffffffffffffffff1681526020016000151581525090565b828054828255906000526020600020906002028101928215610edb5760005260206000209160020282015b82811115610eda578282600082015481600001556001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505091600201919060020190610e4e565b5b509050610ee89190610eec565b5090565b610f3791905b80821115610f33576000808201600090556001820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600201610ef2565b5090565b905600a165627a7a723058204ef3acada3d52fcd70b7fe165cf0d1bf7c525721dba3db9209c0c4272c4c53db0029";

  // source sol: src/main/resources/Simple_1.sol
  public static String SimpleCode_1 = "6080604052600160005534801561001557600080fd5b5060df806100246000396000f3006080604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360fe47b114604e5780636d4ce63c146078575b600080fd5b348015605957600080fd5b5060766004803603810190808035906020019092919050505060a0565b005b348015608357600080fd5b50608a60aa565b6040518082815260200191505060405180910390f35b8060008190555050565b600080549050905600a165627a7a723058202dd6056ea84968f05202910ca070fe13f6f46ff5507867f313d9c98bf2d2e55c0029";

  // source sol: src/main/resources/Simple_2.sol
  public static String SimpleCode_2 = "6080604052600260005534801561001557600080fd5b5060df806100246000396000f3006080604052600436106049576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360fe47b114604e5780636d4ce63c146078575b600080fd5b348015605957600080fd5b5060766004803603810190808035906020019092919050505060a0565b005b348015608357600080fd5b50608a60aa565b6040518082815260200191505060405180910390f35b8060008190555050565b600080549050905600a165627a7a72305820033c553840f888d3646d24ee5245c24ad10f2915b39adc2eee4cbf27d7b2f6e00029";

  // define contract info address
  public static Address addressCert = new Address("0x0000000000000000000000000000000000000001");

  // define version
  public static Uint versionOne = new Uint256(new BigInteger("1"));
  public static Uint versionTwo = new Uint256(new BigInteger("2"));

  // the addresses of ControlVersion,Simple_1,Simple_2
  public static String contractAddress;
  public static String simpleAddressOne;
  public static String simpleAddressTwo;


  public static void main(String[] argv) throws IOException, InterruptedException {

    // deploy contracts
    System.out.println();
    initContracts();

    // add contract versions
    System.out.println();
    addContract(addressCert, simpleAddressOne, versionOne);
    addContract(addressCert, simpleAddressTwo, versionTwo);
    Thread.sleep(6000);

    // run different version contract
    System.out.println();
    Function getFunc = new Function(
        "get",
        Arrays.asList(),
        Arrays.asList(new TypeReference<Uint>() {
        })
    );
    runContract(addressCert, versionOne, getFunc);
    runContract(addressCert, versionTwo, getFunc);


  }

  private static Type getAppCallResult(String addr, Function func)
      throws IOException {
    String funcData = FunctionEncoder.encode(func);
    Call call = new Call(adminPublicAddress, addr, funcData);
    String result = service.appCall(call, DefaultBlockParameterName.PENDING)
        .send().getValue();
    return FunctionReturnDecoder.decode(result, func.getOutputParameters()).get(0);
  }

  public static AppSendTransaction appSendRawTransaction(String code) throws IOException {

    String nonce = TestUtil.getNonce();
    BigInteger validUtil = TestUtil.getValidUtilBlock(service);
    Transaction rtx = Transaction.createContractTransaction(
        nonce, 10000000, validUtil.longValue(),
        version, chainId, "0", code);
    String signedTransaction = rtx.sign(adminPrivateKey, cryptoTx, false);
    AppSendTransaction appSendTx = service
        .appSendRawTransaction(signedTransaction).send();
    return appSendTx;
  }

  public static void addContract(Address addressCert, String addressContract, Uint versionContract)
      throws IOException {

    List<Type> inputParameters = Arrays
        .asList(addressCert,
            versionContract, new Address(addressContract));
    String funcData = CITASystemContract.encodeFunction("add_address", inputParameters);
    String txHash = CITASystemContract.sendTxAndGetHash(
        contractAddress, service, adminPrivateKey, funcData, version, chainId);
    System.out.println("add version " + versionContract.getValue() + " txHash: " + txHash);

  }

  public static void initContracts() throws IOException, InterruptedException {

    AppMetaData appMetaData;
    appMetaData = service.appMetaData(DefaultBlockParameter.valueOf("latest")).send();
    String chainIdHex = appMetaData.getAppMetaDataResult().getChainIdV1();
    chainId = new BigInteger(chainIdHex.substring(2), 16);
    version = appMetaData.getAppMetaDataResult().getVersion();

    // Deploy ContractVersion Contract
    AppSendTransaction appSendTx = appSendRawTransaction(ContractVersionCode);
    String validTransactionHash = appSendTx.getSendTransactionResult().getHash();
    Thread.sleep(6000);
    AppGetTransactionReceipt appGetTransactionReceipt = service.appGetTransactionReceipt(
        validTransactionHash).send();
    TransactionReceipt transactionReceipt =
        appGetTransactionReceipt.getTransactionReceipt();
    contractAddress = transactionReceipt.getContractAddress();

    // Deploy Simple Versions Contract
    AppSendTransaction appSendTx_1 = appSendRawTransaction(SimpleCode_1);
    AppSendTransaction appSendTx_2 = appSendRawTransaction(SimpleCode_2);
    String validTransactionHash_1 = appSendTx_1.getSendTransactionResult().getHash();
    String validTransactionHash_2 = appSendTx_2.getSendTransactionResult().getHash();
    Thread.sleep(8000);
    AppGetTransactionReceipt appGetTransactionReceipt_1 = service.appGetTransactionReceipt(
        validTransactionHash_1).send();
    TransactionReceipt transactionReceipt_1 =
        appGetTransactionReceipt_1.getTransactionReceipt();
    AppGetTransactionReceipt appGetTransactionReceipt_2 = service.appGetTransactionReceipt(
        validTransactionHash_2).send();
    TransactionReceipt transactionReceipt_2 =
        appGetTransactionReceipt_2.getTransactionReceipt();
    simpleAddressOne = transactionReceipt_1.getContractAddress();
    simpleAddressTwo = transactionReceipt_2.getContractAddress();

    System.out.println("Contract-Version Contract: " + contractAddress);
    System.out.println("Simple version one  Contract: " + simpleAddressOne);
    System.out.println("Simple version two  Contract: " + simpleAddressTwo);
  }

  public static void runContract(Address address, Uint version, Function function)
      throws IOException {

    System.out.println("Run " + address.toString() + " version " + version.getValue() + ":");
    Function getAddressFunc = new Function(
        "get_address",
        Arrays.asList(address, version),
        Arrays.asList(new TypeReference<Address>() {
        })
    );
    Address addressReal = (Address) getAppCallResult(contractAddress, getAddressFunc);
    System.out.println("The true run address: " + addressReal);

    Uint result = (Uint) getAppCallResult(addressReal.toString(), function);
    System.out.println("The run result :" + result.getValue());

  }

}
