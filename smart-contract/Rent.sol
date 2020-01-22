pragma solidity >=0.4.22 <0.6.0;

contract Rent {

  address payable private master;

  modifier onlyMaster() {
      require(master == msg.sender, "Only the master can call this.");
      _;
  }

  constructor() public {
      master = msg.sender;
  }

  function kill() external onlyMaster {
    selfdestruct(master);
  }
}