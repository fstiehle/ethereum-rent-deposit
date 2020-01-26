// Modiefied from: https://github.com/ethereum/solc-js/issues/114#issuecomment-354752466
// And from: https://github.com/ethereum/solc-js/blob/master/README.md

const solc = require('solc');
const fs = require('fs');

const inputs = {
  language: 'Solidity',
  sources: {
    'RentFactory.sol': {
      content: fs.readFileSync('RentFactory.sol').toString()
    }
  },
  settings: {
    outputSelection: {
      '*': {
        '*': ['*']
      }
    }
  }
};

// Assumes imported files are in the same folder/local path
function findImports(path) {
  return {
    'contents': fs.readFileSync(path).toString()
  }
}

const compiledCode = solc.compile(JSON.stringify(inputs), { import: findImports })

fs.writeFile('compiled.json', compiledCode, function(err) {
  if (err) throw err;
  console.log('Compiled & saved');
});