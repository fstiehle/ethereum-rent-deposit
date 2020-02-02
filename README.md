# blockchain-rent-deposit
dApp for rent deposit and settlement - keeps confidential data off-chain

## Abstract
In a traditional renting contract the deposit value is held by a trusted third party. In many instances, this third party may be the landlord themselves. This work presents a Proof-of-Concept implementation for trustless renting deposit contracts. Confidential data of a given landlord and property is kept of-chain. A hash over this data can be used to link a legal contract, or a real-world property, to a given smart contract deployed on Ethereum. The smart contract is created from a factory contract to ensure trust in the codebase. The correctness of the factory contract and the instances it spawns can be verified by everyone at any time. A GUI is deployed to make entering data as convenient as possible. Once the contract is deployed from the factory, each party can use a wallet or any other means they trust to interact with the renting contract directly without having to go through and trust our system. This system provides some clear advantages over traditional solutions like notaries, mainly: availability, affordability, and ease of use.

## System Architecture 

![System Architecture](https://raw.githubusercontent.com/fstiehle/blockchain-rent-deposit/master/docs/architecture.png)

### _Frontend Service_
The Frontend service is responsible for serving the website to the user and relaying data to the Resource Service. See

### _Resource Service_
The Resource service manages the confidential data in the Database Service, it receives data from the Frontend Service, checks the renting property for uniqueness and relays the non-confidential data to the Rent Factory.

### _Rent Factory Smart Contract_
The Rent Factory instantiates new Rent Contracts based on the data it receives by the Resource Service.

### _Rent_
The Rent Smart Contract is instantiated by the Rent Factory. Tenant and Landlord can interact through their wallets directly with this contract. A tenant can deposit and accept a rent contract by sending a normal Ether transfer transaction (Like to any regular account). 
