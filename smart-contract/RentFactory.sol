pragma solidity ^0.5.0;

import './node_modules/@openzeppelin/contracts/ownership/Ownable.sol';
import './Rent.sol';

contract RentFactory is Ownable {

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