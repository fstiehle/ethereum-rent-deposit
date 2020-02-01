const assert = require('assert');
const Web3 = require('web3')

const runTest = async (contract, provider, web3, rentContractAbi) => {

  var address;
  
  console.log("Create new rent contract")
  await contract.methods.createInstance(
    provider.addresses[1],
    provider.addresses[2],
    500000,
    Date.now(),
    new Date(Date.now()).setFullYear(new Date(Date.now()).getFullYear() + 1),
    2
  ).send({
    from: provider.addresses[0],
    gas: 9999999999999
  })
  .then(
    (e) => {
      address = e.events['ContractCreated'].returnValues[1];
      console.log("New rent contract at: " + address);
    },
    (error) => assert.ok(false))

  console.log("Test Rent contract")
  const rentContract = new web3.eth.Contract(rentContractAbi, address)

  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {
      console.log(e)
    },
    (error) => console.log(error));

  console.log("Accept contract...")
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[2],
    value: 500000
  });

  console.log("Check new settlement status...")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {      
      assert(e[1][0] == 500000)
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });

    console.log("Check contract now active...")
    await rentContract.methods.active().call({
      from: provider.addresses[0],
    })
    .then(
      (e) => {
        console.log("Contract now active? " + e);
        assert(true == e);
      },
      (error) => {
        console.log(error);
        assert.ok(false);
      });
}

module.exports = runTest;