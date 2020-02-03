pragma solidity ^0.5.0;

contract Rent {

  /**
    The time window a landlord is able to make a claim
    after the contract ends
   */
  uint constant claimWindow = 2419200; // 4 weeks
  uint constant timeout = 31536000; // 1 year

  address payable public landlord;
  address public tenant;
  uint256 public depositWei;
  int public integrityHash;

  uint256 public settleLandlord;
  uint256 public settleTenant;
  bool public settlement = true;
  bool public active = false;

  // Unix timestamps
  uint public startTime;
  uint public expirationTime;
  uint public claimMade;

  event SettlementOffer(address bidder, uint amount);
  event DepositWithdrawn(address account, uint amount);
  event SettlementReached(uint tenant, uint landlord);

  constructor(
    address payable _landlord,
    address _tenant,
    uint256 _depositWei,
    uint _startTime,
    uint _expirationTime,
    int _integrityHash
    )
    public
  {
    landlord = _landlord;
    tenant = _tenant;
    depositWei = _depositWei;
    integrityHash = _integrityHash;
    startTime = _startTime;
    expirationTime = _expirationTime;
  }

  /**
    Tenant can accept contract if deposit has been made
   */
  function acceptContract() external payable onlyTenant {
    proccessPayment();
  }

  /**
    Everyone can make a payment but only tenant can accept contract
   */
  function() external payable {
    require(0 == msg.data.length, "Invalid function called");
    if (tenant == msg.sender) {
      proccessPayment();
    }
  }

  function proccessPayment() internal {
    if (address(this).balance >= depositWei) {
      settleTenant = depositWei;
      settlement = true;
      active = true;
    }
  }

  /**
    Landlord can make a claim to start settlement phase
   */
  function makeClaim() external onlyLandlord {
    require(true == active, "Tenant has not accepted this contract yet");
    // Our contract can tolerate slight timestamp variation
    // See: https://link.medium.com/1J8eBAxSy3
    require(expirationTime < block.timestamp, "Rent contract still active");
    require(expirationTime + claimWindow > block.timestamp, "Claim window expired");
    settlement = false;
    settleLandlord = depositWei;
    settleTenant = depositWei;
    claimMade = block.timestamp;
  }

  /**
    Settle on a share of the deposit if a claim has been made
    @param _settle amount in wei to claim from deposit
   */
  function settle(uint256 _settle) external onlyParty {
    require(true == active, "Tenant has not accepted this contract yet");
    require(false == settlement, "Settlement already reached");
    require(_settle <= depositWei, "Settlement can not be higher than deposit");

    if (msg.sender == landlord) {
      settleLandlord = _settle;
    } else if (msg.sender == tenant) {
      settleTenant = _settle;
    }
    emit SettlementOffer(msg.sender, _settle);

    if (settleLandlord + settleTenant == depositWei) {
      settlement = true;
      emit SettlementReached(settleTenant, settleLandlord);
    }
  }

  /**
    Tenant or Landlord withdraw their share
    Use Checks-Effects-Interactions Pattern
   */
  function withdraw() external onlyParty {
    require(true == active, "Contract is inactive - accept it by Tenant to proceed");
    require(expirationTime + claimWindow < block.timestamp, "Rent contract still active");
    require(0 < address(this).balance, "All funds already withdrawn, check log for DepositWithdrawn");
    uint toSend = 0;

    if (block.timestamp > expirationTime + timeout && msg.sender == landlord) {
      toSend = address(this).balance;
      settleLandlord = 0;
      settleTenant = 0;

    } else if (true == settlement) {

      if (msg.sender == landlord) {
        toSend = settleLandlord;
        settleLandlord = 0;
      }

      if (msg.sender == tenant) {
        toSend = settleTenant;
        settleTenant = 0;
      }
    }

    if (0 == toSend) {
      return;
    }

    if (0 == settleTenant && 0 == settleLandlord) {
      active = false;
    }

    msg.sender.transfer(toSend);
    emit DepositWithdrawn(msg.sender, toSend);
  }

  function currentSettlementStatus() external view returns (address[2] memory, uint[2] memory) {
    return ([tenant, landlord], [settleTenant, settleLandlord]);
  }

  function kill() external onlyLandlord {
    require(false == active && 0 == settleLandlord && 0 == settleTenant, "Only inactive contract without funds can be killed");
    selfdestruct(landlord);
  }

  modifier onlyLandlord {
    require(
        msg.sender == landlord,
        "Only the landlord can call this function."
    );
    _;
  }

  modifier onlyTenant {
    require(
        msg.sender == tenant,
        "Only the tenant can call this function."
    );
    _;
  }

  modifier onlyParty {
    require(
        msg.sender == landlord || msg.sender == tenant,
        "Only the landlord or the tenant can call this function."
    );
    _;
  }

}