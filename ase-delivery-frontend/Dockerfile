# for local dev
FROM node:16-alpine
# Create app directory
WORKDIR  /frontend

ENV PATH="./node_modules/.bin:$PATH"

COPY . .

EXPOSE 3000

CMD ["npm", "run", "start"]



