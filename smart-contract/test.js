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
  
  //Test#1 Contract can be accepted only by tenant
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[3],
    value: 500000
  }).then(
      (e) => {
        console.log("Contract is accepted by non-tenant, error!");
        assert(false);
      },
      (error) => {
        console.log("OK!");
      });
  
  
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[2],
    value: 500000
  });

  //Test#2 settleTenant should be equal to sent value
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
	
  //Test#3 	Contract active parameter should be equal to true
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
	
	//Test#4 Only Landlord can make a claim
	
  console.log("Make claim by non-landlord")
  await rentContract.methods.makeClaim().call({
    from: provider.addresses[3],
  })
  .then(
    (e) => {
	  console.log("Claim is successfully made not by landlord, error!");
	  assert(false);
    },
    (error) => {
	  console.log("OK!");
    });
	
	
  //Test#5 Claim can be made only within claim window
  
  console.log("Make claim...")
  await rentContract.methods.makeClaim().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {
	  console.log("Claim is successfully made outside the claim window, error!");
	  assert(false);
    },
    (error) => {
	  console.log("OK!");
    });
	
	
	
	
  //Test#6 Make correct claim
  
  console.log("Make correct claim")
  await rentContract.methods.makeClaim().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {
	  console.log("Claim is made successfully");	  
    },
    (error) => {
	  console.log(error);
	  assert.ok(false);
    });
	
  //Test#7 Kill contract by non-landlord
  
  console.log("Killing non-settled contract")
  await rentContract.methods.kill().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("Non-settled contract killed, error!");
	  assert.ok(false);
    },
    (error) => {
      console.log("OK!");      
    });
	
  //Test#8 Landlord settlement
  
  console.log("Make settlement by Landlord")
  await rentContract.methods.settle(100000).call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {
	  console.log("Landlord claim is made successfully");	  
    },
    (error) => {
	  console.log('Some error occured while Landlord settlement');
	  console.log(error);
	  assert.ok(false);
    });
	
  //Test#9 Tenant settlement
  
  console.log("Make settlement by Tenant")
  await rentContract.methods.settle(400000).call({
    from: provider.addresses[2],
  })
  .then(
    (e) => {
	  console.log("OK!");	  
    },
    (error) => {
	  console.log('Some error occured while Landlord settlement');
	  console.log(error);
	  assert.ok(false);
    });
	
 //Test#10 Test settlement status
 
  console.log("Test settlement reach")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {      
      assert(e[1][0] + e[1][1] == 500000)
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });
	
  //Test#11 Withdraw funds by non-party
  
  console.log("Withdraw funds by non-party")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[3],
  })
  .then(
    (e) => {      
      console.log("Withdrawn funds by non-party finished successfully, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });
	
  //Test#12 Withdraw funds by tenant
	
  console.log("Withdraw funds by Tenant")
  await rentContract.methods.withdraw().call({
    from: provider.addresses[2],
  })
  .then(
    (e) => {      
      console.log("OK!");
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });
	
  //Test#13 Withdraw funds by Landlord
	
  console.log("Withdraw funds by Landlord")
  await rentContract.methods.withdraw().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("OK!");
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });  
  
	
  //Test#14 Check all funds are withdrawn
  
  console.log("Check all funds are withdrawn")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {      
      console.log("Some funds weren't withdrawn, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });
	
	
  //Test#15 Kill contract by non-landlord
  
  console.log("Kill contract by non-landlord")
  await rentContract.methods.kill().call({
    from: provider.addresses[3],
  })
  .then(
    (e) => {      
      console.log("Contract killed by non-landlord, error!");
	  assert.ok(false);
    },
    (error) => {
      console.log("OK!");      
    });
	
  //Test#16 Kill contract by landlord
	
  console.log("Kill contract by landlord")
  await rentContract.methods.kill().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("OK!");
    },
    (error) => {
      console.log(error);	  
      assert.ok(false);
    });
  
}



module.exports = runTest;