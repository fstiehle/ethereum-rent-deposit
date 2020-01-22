const HDWalletProvider = require("@truffle/hdwallet-provider")
//load single private key as string
const provider = new HDWalletProvider("hope cabin bone sunset thrive eight tray rubber earth resemble survey nasty", "http://localhost:8545", 0, 5)

const Web3 = require('web3')
const fs = require('fs')
const tests = require('./test')
const web3 = new Web3(provider)

web3.eth.getBlockNumber()
.then((n) => console.log("Current Block Number: " + n))
.catch((e) => console.log(e))

// --- Deploy --- //
const abi = JSON.parse(fs.readFileSync("Rent_sol_Rent.abi"))
const binCode = fs.readFileSync("Rent_sol_Rent.bin").toString()

const contract = new web3.eth.Contract(abi)
contract.deploy({
  data: '0x' + binCode
}).send({
  from: provider.addresses[0], gas: 5372400,
})
.on('error', console.log)
.then((instance) => {

  console.log("Contract Address: " + instance.options.address) 

  console.log("Run tests ...")
  tests(instance, provider)

});

provider.engine.stop()