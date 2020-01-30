// Modiefied from: https://github.com/ethereum/solc-js/issues/114#issuecomment-354752466
// And from: https://github.com/ethereum/solc-js/blob/master/README.md

const solc = require('solc');
const fs = require('fs');

const CONTRACT = 'RentFactory.sol';

const inputs = {
  language: 'Solidity',
  sources: {
    CONTRACT: {
      content: fs.readFileSync(CONTRACT).toString()
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

const compiledCode = JSON.parse(solc.compile(JSON.stringify(inputs), { import: findImports }))

if (compiledCode.hasOwnProperty('errors')) {
  for (var error of compiledCode.errors) {
    console.log(error.formattedMessage);
  }
  return;
}

fs.writeFile('compiled.json', compiledCode, function(err) {
  if (err) throw err;
  console.log('Compiled & saved');
});