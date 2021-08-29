import store from '@/store';

export function getLocaleDateDisplay(dt: Date, locale = 'en-US', format: 'date' | 'datetime' = 'datetime'): string {
  if (!dt) {
    return '';
  }
  if (format === 'date') {
    return new Intl.DateTimeFormat(locale, { year: 'numeric', month: 'short', day: 'numeric' }).format(dt);
  }
  return new Intl.DateTimeFormat(locale, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
  }).format(dt);
}

export function getLocaleNumberDisplay(n: number, locale = 'en-US'): string {
  if (!n) {
    return '0';
  }
  return new Intl.NumberFormat(locale, { maximumSignificantDigits: 3 }).format(n);
}

export function commitJwtTokenToStore(jwt: string, userId: number, user: string, role: string, userName: string): void {
  store.commit('jwt', jwt);
  store.commit('jwtTime', new Date().toISOString());
  store.commit('userId', userId);
  store.commit('user', user);
  store.commit('role', role);
  store.commit('userName', userName);
}

// eslint-disable-next-line @typescript-eslint/ban-types
/*
export function debounce(fn:Function, delay:number): Function {
  let timeoutId:unknown = null;
  return (...args: unknown[]) => {
    clearTimeout(timeoutId as number);
    // const args = arguments;
    const context = this; // eslint-disable-line @typescript-eslint/no-this-alias
    timeoutId = setTimeout(() => {
      // fn.apply(that, args);
      fn.apply(context, args);
    }, delay);
  };
}
*/

// eslint-disable-next-line @typescript-eslint/ban-types
export function debounce<F extends(...params: unknown[]) => void>(fn: F, delay: number): Function {
  let timeoutId: number | null = null;
  return function (this: unknown, ...args: unknown[]) { // eslint-disable-line func-names
    clearTimeout(timeoutId as number);
    timeoutId = window.setTimeout(() => fn.apply(this, args), delay);
  } as F;
}

export function exportFile(data: any, fileName: string) {
  var contentType = "application/vnd.ms-excel";
  const a = document.createElement("a");
  var blob1 = b64toBlob(data, contentType, "");
  var url = URL.createObjectURL(blob1);
  a.href = url;
  a.download = fileName;
  a.click();
  setTimeout(() => {
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }, 0);
}

export function exportFileDocx(data: any, fileName: string) {
  var contentType = "application/msword";
  const a = document.createElement("a");
  var blob1 = b64toBlob(data, contentType, "");
  var url = URL.createObjectURL(blob1);
  a.href = url;
  a.download = fileName;
  a.click();
  setTimeout(() => {
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }, 0);
}

function b64toBlob (b64Data: any, contentType: any, sliceSize: any){
  contentType = contentType || "";
  var sliceSize = sliceSize || 512;
  var byteCharacters = atob(b64Data);
  var byteArrays = [];

  for (
    var offset = 0;
    offset < byteCharacters.length;
    offset += sliceSize
  ) {
    var slice = byteCharacters.slice(offset, offset + sliceSize);

    var byteNumbers = new Array(slice.length);
    for (var i = 0; i < slice.length; i++) {
      byteNumbers[i] = slice.charCodeAt(i);
    }

    var byteArray = new Uint8Array(byteNumbers);

    byteArrays.push(byteArray);
  }
  var blob = new Blob(byteArrays, { type: contentType });
  return blob;
};
