export function slideOffScreen(selector) {
    const elem = document.querySelector(selector);
    elem.animate([{ transform: `translateX(${screen.width}px)` }], {
        duration: 500,
        iterations: 1,
    });
    setTimeout(() => (elem.style.display = 'none'), 500);
}

export function registerToast(selector) {
    setTimeout(() => slideOffScreen(selector), 5000);
}
