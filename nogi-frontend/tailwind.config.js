/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}',
    './node_modules/flowbite/**/*.js'
  ],
  theme: {
    screens: {
      sm: { min: '576px' },
      md: { min: '768px' },
      lg: { min: '992px' }
    },
    extend: {
      colors: {
        main: '#23263F',
        action: '#2F6BCC',
        danger: '#C12E3A',
        warning: '#C98A2A',
        neutral: '#9ca3af'
      },
      fontFamily: {
        noto_sans_l: ['NotoSansKR-Light'],
        noto_sans_r: ['NotoSansKR-Regular'],
        noto_sans_b: ['NotoSansKR-Bold'],
        noto_sans_m: ['NotoSansKR-Medium']
      },
      keyframes: {
        blink: {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '0.5' }
        }
      },
      animation: {
        blink: 'blink 3.5s ease-in-out infinite'
      }
    }
  },
  plugins: [require('flowbite/plugin')]
};
