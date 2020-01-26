const assert = require('assert');
const Web3 = require('web3')

const runTest = async (contract, provider) => {
  
  console.log("Create new rent contract")
  await contract.methods.createInstance(
    provider.addresses[1],
    provider.addresses[2],
    500000,
    Date.now(),
    new Date(Date.now()).setFullYear(new Date(Date.now()).getFullYear() + 1),
    Web3.utils.fromAscii("Hash")
  ).call({
    from: provider.addresses[0] 
  })
  .then(
    (e) => console.log("New rent contract at: " + e), // throw error if succees
    (error) => assert.ok(false))
}

module.exports = runTest;