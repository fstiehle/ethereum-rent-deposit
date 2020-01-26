const HDWalletProvider = require("@truffle/hdwallet-provider")
//load single private key as string
const provider = new HDWalletProvider("hope cabin bone sunset thrive eight tray rubber earth resemble survey nasty", "http://localhost:8545", 0, 5)

const Web3 = require('web3')
const fs = require('fs')
const tests = require('./test')
const web3 = new Web3(provider)

const contractName = 'RentFactory';

web3.eth.getBlockNumber()
.then((n) => console.log("Current Block Number: " + n))
.catch((e) => console.log(e))

// --- Deploy --- //
compiledCode = JSON.parse(fs.readFileSync('compiled.json'));

const abi = compiledCode.contracts[contractName + '.sol'][contractName].abi;
const binCode = compiledCode.contracts[contractName + '.sol'][contractName].evm.bytecode.object;

const contract = new web3.eth.Contract(abi)
contract.deploy({
  data: '0x' + binCode
}).send({
  from: provider.addresses[0], gas: 9999999999999,
})
.on('error', console.log)
.then((instance) => {

  console.log("Contract Address: " + instance.options.address) 

  console.log("Run tests ...")
  tests(instance, provider)

});

provider.engine.stop()