pragma solidity ^0.5.0;

import './node_modules/@openzeppelin/contracts/ownership/Ownable.sol';
import './Rent.sol';

contract RentFactory is Ownable {

  /**
    Creates new rent contract
    @param landlord Landlords public key
    @param tenant Tenants public key
    @param depositWei Deposit Amount in Wei
    @param startTime UTC timestamp of the start date of the contract
    @param expirationTime UTC timestamp of the end date of the contract
    @param integrityHash Hash of integrity data kept off-chain
    @return address New contract address
   */
  function createInstance(
    address payable landlord,
    address payable tenant,
    uint256 depositWei,
    uint startTime,
    uint expirationTime,
    bytes memory integrityHash
    )
    public onlyOwner returns (address)
  {
    return address(new Rent(landlord, tenant, depositWei, startTime, expirationTime, integrityHash));
  }

}