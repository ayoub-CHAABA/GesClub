import dayjs from 'dayjs';
import { IClub } from 'app/shared/model/club.model';
import { Statut } from 'app/shared/model/enumerations/statut.model';

export interface IEvent {
  id?: number;
  title?: string;
  eventDate?: string;
  eventEnd?: string;
  eventPlace?: string;
  content?: string | null;
  statut?: Statut | null;
  budget?: string;
  club?: IClub;
}

export const defaultValue: Readonly<IEvent> = {};
