const assert = require('assert');

let Web3;

const runTest = async (contract, provider, web3, rentContractAbi) => {

  Web3 = web3;
  var address;
  var rentContract;
  var initBlock;
  var initTimestamp;


  console.log("============   Scenario 1: Contract without claim   ============")

  initBlock = await advanceTimeAndBlock(0);
  initTimestamp = initBlock.timestamp;
  
  console.log("Create new rent contract")
  await contract.methods.createInstance(
    provider.addresses[1],
    provider.addresses[2],
    500000,
    initTimestamp,
    initTimestamp + 31536000,
    2
  ).send({
    from: provider.addresses[0],
    gas: 5000000
  })
  .then(
    (e) => {
      address = e.events['ContractCreated'].returnValues[1];
      console.log("New rent contract at: " + address);
    },
    (error) => assert.ok(false))
	
  

  console.log("Test Rent contract")
  rentContract = new web3.eth.Contract(rentContractAbi, address)

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
  
  //Test#2 Accept contract by tenant
  console.log("Accept contract by tenant...") 
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[2],
    value: 500000
  }).then(
    (e) => {
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });

  //Test#3 settleTenant should be equal to sent value
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
	
  //Test#4  Contract active parameter should be equal to true
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

  //Test#5 Check impossibility to kill active contract
  
  console.log("Check impossibility to kill active contract")
  await rentContract.methods.kill().call({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("Active contract killed, error!");
	  assert.ok(false);
    },
    (error) => {
      console.log("OK!");      
    });

	
  console.log("One year, four weeks and one day advancement");
  await advanceTimeAndBlock(34041600);
  

  //Test#6 Check impossibility to make claim after claim window
  
  console.log("Check impossibility to make claim after claim window")
  await rentContract.methods.makeClaim().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {
	  console.log("Claim is successfully made after claim window, error!");
	  assert(false);
    },
    (error) => {
	  console.log("OK!");
    });
  
  //Test#7 Check impossibility to withdraw funds by non-party
  
  console.log("Check impossibility to withdraw funds by non-party")
  await rentContract.methods.withdraw().send({
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

  //Test#8 Withdraw funds by Tenant
	
  console.log("Withdraw funds by Tenant")
  await rentContract.methods.withdraw().send({
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
  
	
  //Test#9 Check all funds are withdrawn
  
  console.log("Check all funds are withdrawn")
  await rentContract.methods.withdraw().send({
    from: provider.addresses[2],
  })
  .then(
    (e) => {      
      console.log("Some funds weren't withdrawn, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });
	
	
  //Test#10 Check impossibility to kill contract by non-landlord
  
  console.log("Check impossibility to kill contract by non-landlord")
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
	
  //Test#11 Kill contract by landlord
	
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
	
  console.log("Scenario1 tests passed successfully");
  console.log("");
  console.log("");
  console.log("");



  console.log("============   Scenario 2: Contract with resolved claim   ============")

  initBlock = await advanceTimeAndBlock(0);
  initTimestamp = initBlock.timestamp;
  
  console.log("Create new rent contract")
  await contract.methods.createInstance(
    provider.addresses[1],
    provider.addresses[2],
    500000,
    initTimestamp,
    initTimestamp + 31536000,
    2
  ).send({
    from: provider.addresses[0],
    gas: 5000000
  })
  .then(
    (e) => {
      address = e.events['ContractCreated'].returnValues[1];
      console.log("New rent contract at: " + address);
    },
    (error) => assert.ok(false))
	
  

  console.log("Test Rent contract")
  rentContract = new web3.eth.Contract(rentContractAbi, address)

  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {
      console.log(e)
    },
    (error) => console.log(error));

  //Test#12 Only Landlord can make a claim
	
  console.log("Make claim by non-landlord")
  await rentContract.methods.makeClaim().send({
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

  //Test#13 Check impossibility to makeClaim without Tenant acceptance
  
  console.log("Check impossibility to withdraw funds from unsettled contract")
  await rentContract.methods.makeClaim().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("Withdrawn funds from unsettled contract, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });

  console.log("Accept contract...")
  
  //Test#14 Accept contract by tenant
  console.log("Accept contract by tenant...") 
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[2],
    value: 500000
  }).then(
    (e) => {
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });	
	
  //Test#15 Check impossibility to make claim before expiration date
  
  console.log("Check impossibility to make claim before expiration date")
  await rentContract.methods.makeClaim().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {
	  console.log("Claim is successfully made before expiration date, error!");
	  assert(false);
    },
    (error) => {
	  console.log("OK!");
    });
	
	
	
  console.log("One year and one day advancement");
  await advanceTimeAndBlock(31622400);


	
  //Test#16 Make claim
  
  console.log("Make claim")
  await rentContract.methods.makeClaim().send({
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
	
  
	
  //Test#17 Landlord settlement
  
  console.log("Make settlement by Landlord")
  await rentContract.methods.settle(100000).send({
    from: provider.addresses[1],
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
	
  //Test#18 Tenant settlement
  
  console.log("Make settlement by Tenant")
  await rentContract.methods.settle(400000).send({
    from: provider.addresses[2],
  })
  .then(
    (e) => {
	  console.log("OK!");	  
    },
    (error) => {
	  console.log('Some error occured while Tenant settlement');
	  console.log(error);
	  assert.ok(false);
    });
	
 //Test#19 Test settlement status
 
  console.log("Test settlement reach")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {
      assert(parseInt(e[1][0]) + parseInt(e[1][1]) == 500000)
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });
	
  console.log("Four weeks and one day advancement");
  await advanceTimeAndBlock(2505600);
  
  
	
	
 	
  //Test#20 Withdraw funds by Landlord
	
  console.log("Withdraw funds by Landlord")
  await rentContract.methods.withdraw().send({
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


  console.log("One year and one day advancement");
  await advanceTimeAndBlock(31622400);


  //Test#21 Check possibility to withdraw funds by tenant after timeout
	
  console.log("Check possibility to withdraw funds by tenant after timeout")
  await rentContract.methods.withdraw().send({
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
	
  //Test#22 Check all funds are withdrawn
  
  console.log("Check all funds are withdrawn")
  await rentContract.methods.withdraw().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("Some funds weren't withdrawn, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });
	
  //Test#23 Kill contract by landlord
	
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
	
  console.log("Scenario2 tests passed successfully");
  console.log("");
  console.log("");
  console.log("");



  console.log("============   Scenario 3: Contract with non-resolved claim   ============")

  initBlock = await advanceTimeAndBlock(0);
  initTimestamp = initBlock.timestamp;
  
  console.log("Create new rent contract")
  await contract.methods.createInstance(
    provider.addresses[1],
    provider.addresses[2],
    500000,
    initTimestamp,
    initTimestamp + 31536000,
    2
  ).send({
    from: provider.addresses[0],
    gas: 5000000
  })
  .then(
    (e) => {
      address = e.events['ContractCreated'].returnValues[1];
      console.log("New rent contract at: " + address);
    },
    (error) => assert.ok(false))
	
  

  console.log("Test Rent contract")
  rentContract = new web3.eth.Contract(rentContractAbi, address)

  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {
      console.log(e)
    },
    (error) => console.log(error));

  console.log("Accept contract...")
  
  //Test#24 Accept contract by tenant
  console.log("Accept contract by tenant...") 
  await rentContract.methods.acceptContract().send({
    from: provider.addresses[2],
    value: 500000
  }).then(
    (e) => {
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });
	
	
	
  console.log("One year and one day advancement");
  await advanceTimeAndBlock(31622400);


	
  //Test#25 Make claim
  
  console.log("Make claim")
  await rentContract.methods.makeClaim().send({
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
	
  
	
  //Test#26 Landlord settlement
  
  console.log("Make settlement by Landlord")
  await rentContract.methods.settle(100000).send({
    from: provider.addresses[1],
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
	
  //Test#27 Tenant overcoming settlement
  
  console.log("Make overcoming settlement by Tenant")
  await rentContract.methods.settle(450000).send({
    from: provider.addresses[2],
  })
  .then(
    (e) => {
	  console.log("OK!");	  
    },
    (error) => {
	  console.log('Some error occured while Tenant settlement');
	  console.log(error);
	  assert.ok(false);
    });
	
 //Test#28 Test settlement not reached
 
  console.log("Test settlement not reached")
  await rentContract.methods.currentSettlementStatus().call({
    from: provider.addresses[0],
  })
  .then(
    (e) => {
      assert(parseInt(e[1][0]) + parseInt(e[1][1]) > 500000)
      console.log('OK!');
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });
	
	
	
  console.log("Four weeks and one day advancement");
  await advanceTimeAndBlock(2505600);
  
  
	
	
  //Test#29 Check impossibility to withdraw funds from unsettled contract
  console.log("Check impossibility to withdraw funds from unsettled contract")
  await rentContract.methods.withdraw().send({
    from: provider.addresses[2],
  })
  .then(
    (e) => {      
      assert(0 == Object.keys(e.events).length, 0)
    },
    (error) => {
      assert.ok(false);
    });


  console.log("One year and one day advancement");
  await advanceTimeAndBlock(31622400);

	
  //Test#30 Check impossibility to withdraw funds from unsettled contract by Tenant after timeout
	
  await rentContract.methods.withdraw().send({
    from: provider.addresses[2],
  })
  .then(
    (e) => {      
      assert(0 == Object.keys(e.events).length, 0)
    },
    (error) => {
      assert.ok(false);
    });
	
	
  //Test#31 Withdraw funds by Landlord
	
  console.log("Withdraw funds by Landlord")
  await rentContract.methods.withdraw().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      assert(1 == Object.keys(e.events).length, 0)
    },
    (error) => {
      console.log(error);
      assert.ok(false);
    });  
  
	
  //Test#32 Check all funds are withdrawn
  
  console.log("Check all funds are withdrawn")
  await rentContract.methods.withdraw().send({
    from: provider.addresses[1],
  })
  .then(
    (e) => {      
      console.log("Some funds weren't withdrawn, error!");
      assert.ok(false);
    },
    (error) => {
      console.log("OK!");
    });
	
  //Test#33 Kill contract by landlord
	
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
	
  console.log("Scenario3 tests passed successfully");
  console.log("");
  console.log("");
  console.log("");
  
}



advanceTimeAndBlock = async (time) => {
    await advanceTime(time);
    await advanceBlock();

    return Promise.resolve(Web3.eth.getBlock('latest'));
}

advanceTime = (time) => {
    return new Promise((resolve, reject) => {
        Web3.currentProvider.sendAsync({
            jsonrpc: "2.0",
            method: "evm_increaseTime",
            params: [time],
            id: new Date().getTime()
        }, (err, result) => {
            if (err) { return reject(err); }
            return resolve(result);
        });
    });
}

advanceBlock = () => {
    return new Promise((resolve, reject) => {
        Web3.currentProvider.sendAsync({
            jsonrpc: "2.0",
            method: "evm_mine",
            id: new Date().getTime()
        }, (err, result) => {
            if (err) { return reject(err); }
            const newBlockHash = Web3.eth.getBlock('latest').hash;

            return resolve(newBlockHash)
        });
    });
}

module.exports = runTest;