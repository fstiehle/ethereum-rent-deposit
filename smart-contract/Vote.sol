pragma solidity >=0.4.22 <0.6.0;

contract Vote {

  address payable private master;
  uint8 private votes;

  struct Choice {
      string name;
      uint8 count;
  }

  Choice[] private choices;
  bool[] private voters;
  mapping(address => uint) private voterIndex;
  mapping(string => uint) private choiceIndex;

  modifier onlyMaster() {
      require(master == msg.sender, "Only the master can call this.");
      _;
  }

  modifier onlyVoter() {
      require(voters[voterIndex[msg.sender] - 1] == true, "Not a valid voter or already voted.");
      _;
  }

  constructor() public {
      master = msg.sender;
  }

  function grantVote(address _voter) external onlyMaster {
      require(voters.length < 255, "Max Voters reached.");
      voterIndex[_voter] = voters.push(true);
  }

  // Clean up old Ballot
  function newBallot() external onlyMaster {
      votes = 0;
      delete choices;
      delete voters;
  }

  // solidity doesnt support dynamic string arrays for external functions
  // so either a byte array must be used and serialized to a string or each choice must be added seperately
  // From: https://hackernoon.com/serializing-string-arrays-in-solidity-db4b6037e520
  function addChoice(string calldata _choice) external onlyMaster {
      require(0 == choiceIndex[_choice], "Choice already exists.");
      choiceIndex[_choice] = choices.push(Choice(_choice, 0));
  }

  function vote(string calldata _choice) external onlyVoter {
      uint index = choiceIndex[_choice] - 1;
      choices[index].count = choices[index].count + 1;
      voters[voterIndex[msg.sender] - 1] = false;
      votes = votes + 1;
  }

  function result() external view returns (string memory, uint8) {
    require(votes > voters.length/2, "No Quorum met.");

    Choice memory choice = choices[0];
    for (uint i = 1; i < choices.length; ++i) {
        if (choices[i].count > choice.count) {
            choice = choices[i];
        }
    }
    return (choice.name, choice.count);
  }

  function kill() external onlyMaster {
    require(0 == voters.length && 0 == choices.length || votes > voters.length/2, "Can't kill ongoing vote");
    selfdestruct(master);
  }
}