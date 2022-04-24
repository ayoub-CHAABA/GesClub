import dayjs from 'dayjs';

export interface IClub {
  id?: number;
  clubName?: string;
  creationDate?: string;
  logoContentType?: string | null;
  logo?: string | null;
}

export const defaultValue: Readonly<IClub> = {};
