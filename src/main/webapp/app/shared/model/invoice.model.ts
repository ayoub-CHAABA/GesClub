import { IEvent } from 'app/shared/model/event.model';

export interface IInvoice {
  id?: number;
  amount?: number;
  numInvoice?: string;
  event?: IEvent;
}

export const defaultValue: Readonly<IInvoice> = {};
