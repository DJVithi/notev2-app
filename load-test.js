import http from 'k6/http';

export const options = {
  vus: 500,
  duration: '30s'
};

const params = {
  headers: {
    Authorization: 'Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTc4NDI0MzAyMywiZXhwIjoxNzg0MjQzOTIzfQ.s7Vjm93tQ5RCPFsBhbiC2CGszWXjqZM3AYuRyw9WwNlda3BNJKF_-3A7Gns9mQib'
  }
};

export default function () {
  http.get('http://localhost:8080/notes', params);
}