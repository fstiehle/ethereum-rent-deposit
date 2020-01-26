pragma solidity ^0.5.0;

contract Rent {

  address payable private landlord;
  address payable private tenant;
  uint256 depositWei;
  bytes integrityHash;

  uint256 settleLandlord;
  uint256 settleTenant;
  bool settlement = true;
  bool active = false;

  // Unix timestamps
  uint startTime;
  uint expirationTime;
  uint claimMade;

  event SettlementOffer(address bidder, uint amount);
  event DepositWithdrawn(address account, uint amount);
  event SettlementReached(uint tenant, uint landlord);

  constructor(
    address payable _landlord,
    address payable _tenant,
    uint256 _depositWei,
    uint _startTime,
    uint _expirationTime,
    bytes memory _integrityHash
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
    if (address(this).balance >= depositWei) {
      settleTenant = depositWei;
      active = true;
    }
  }

  /**
    Landlord can make a claim to start settlement phase
   */
  function makeClaim() external onlyLandlord {
    // Our contract can tolerate slight timestamp variation
    // See: https://link.medium.com/1J8eBAxSy3
    require(expirationTime > block.timestamp, "Rent contract still active");
    settlement = false;
    claimMade = block.timestamp;
  }

  /**
    Settle on a share of the deposit if a claim has been made
    @param _settle amount in wei to claim from deposit
   */
  function settle(uint256 _settle) external onlyParty {
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
   */
  function withdraw() external onlyParty {
    uint toSend = 0;
    if (msg.sender == landlord) {
      toSend = settleLandlord;
      settleLandlord = 0;
    } else if (msg.sender == tenant) {
      toSend = settleTenant;
      settleTenant = 0;
    }

    msg.sender.transfer(toSend);
    emit DepositWithdrawn(msg.sender, toSend);
  }

  function currentSettlementStatus() external view returns (address payable[2] memory, uint[2] memory) {
    return ([tenant, landlord], [settleTenant, settleLandlord]);
  }

  function kill(address payable returnTo) private {
    selfdestruct(returnTo);
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