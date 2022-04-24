import dayjs from 'dayjs';
import { IClub } from 'app/shared/model/club.model';
import { Statut } from 'app/shared/model/enumerations/statut.model';

export interface IMeeting {
  id?: number;
  title?: string;
  meetingDate?: string;
  meetingPlace?: string;
  content?: string | null;
  statut?: Statut | null;
  club?: IClub;
}

export const defaultValue: Readonly<IMeeting> = {};
