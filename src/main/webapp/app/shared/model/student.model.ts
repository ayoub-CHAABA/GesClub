import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IClub } from 'app/shared/model/club.model';
import { Membership } from 'app/shared/model/enumerations/membership.model';

export interface IStudent {
  id?: number;
  firstname?: string;
  lastname?: string;
  nationality?: string | null;
  city?: string | null;
  filiere?: string;
  level?: string;
  residency?: string | null;
  tel?: string;
  mail?: string;
  pictureContentType?: string | null;
  picture?: string | null;
  role?: Membership | null;
  adhesion?: string | null;
  user?: IUser;
  club?: IClub | null;
}

export const defaultValue: Readonly<IStudent> = {};
