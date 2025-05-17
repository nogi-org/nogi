import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

// Configure dayjs plugins
dayjs.extend(utc);
dayjs.extend(timezone);

export const toYMDHMS = date => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
};

export const toYMD = date => {
  return dayjs(date).format('YYYY-MM-DD');
};

export const toKRFromUTC = date => {
  return dayjs.utc(date).tz('Asia/Seoul').format('YYYY-MM-DD HH:mm:ss');
};
