FROM node:20.5 as node

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

FROM nginx:alpine
COPY --from=node /app/dist/millenium-falcon-ui /usr/share/nginx/html
EXPOSE 80
