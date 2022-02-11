# Getting Started with ASE Delivery System

## Available Scripts

We will continue with npm instead of yarn for running the frontend.

## Local Dev Run
npm install ;
npm i --save @fortawesome/fontawesome-svg-core ;
npm install --save @fortawesome/free-solid-svg-icons ;
npm install --save @fortawesome/react-fontawesome ;
npm install --save cdbreact ;
npm run start

## Dev Run in Docker
npm install;  
docker build -t ase-frontend-image-dev .   ;
docker run -p 3000:3000 ase-frontend-image-dev ;
