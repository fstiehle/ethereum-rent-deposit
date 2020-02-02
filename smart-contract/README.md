# Build
1. In the folder smart-contract run `npm install` to install dependencies.
2. Include a .env file with the parameters PRIVATE_KEY and API_KEY for deployment to rinkeby. (https://www.npmjs.com/package/dotenv) The .env file has to be located in the smart-contract folder.
3. Run `npm run build` to compile RentFactory.sol and Rent.sol as well as their dependencies.

# Deployment
1. Run `npm run deployprod` to deploy to rinkeby

OR
1. run `npm run ganache` to start a local ganache server
2. run `npm run deploydev` to deploy to the local ganache server and execute automated tests
