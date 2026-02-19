import { HttpInterceptorFn } from '@angular/common/http';

const NONCE_META_SELECTOR = 'meta[name="csp-nonce"]';

export const cspNonceInterceptor: HttpInterceptorFn = (req, next) => {
  const nonce = document.querySelector<HTMLMetaElement>(NONCE_META_SELECTOR)?.content?.trim();

  if (!nonce) {
    return next(req);
  }

  return next(req.clone({
    setHeaders: {
      'X-CSP-Nonce': nonce
    }
  }));
};
