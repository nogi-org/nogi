import dayjs from 'dayjs';

export const toYMDHMs = date => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
};
