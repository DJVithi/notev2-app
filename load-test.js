import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 100,        
  duration: '30s'
};

const payload = JSON.stringify({
  username: 'test',
  password: 'falschesPasswort'
});

const params = {
  headers: {
    'Content-Type': 'application/json'
  }
};
//const params = {
//  headers: {
//    Authorization: 'Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTc4NDI0MzAyMywiZXhwIjoxNzg0MjQzOTIzfQ.s7Vjm93tQ5RCPFsBhbiC2CGszWXjqZM3AYuRyw9WwNlda3BNJKF_-3A7Gns9mQib'
//  }
//};

export default function () {
  const res = http.post('http://localhost:8080/auth/login', payload, params);

  check(res, {
    'ist 401 (falsche Credentials, aber durchgelassen)': (r) => r.status === 401,
    'ist 429 (Rate Limit gegriffen)': (r) => r.status === 429,
  });
}