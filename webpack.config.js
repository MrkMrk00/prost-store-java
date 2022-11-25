const { resolve } = require('path');
const WatchExternalFilesPlugin = require('webpack-watch-files-plugin').default;
const assets = 'src/main/resources/static/build';

module.exports = {
    mode: 'development',
    entry: './frontend/index.js',
    output: {
        path: resolve(__dirname, assets),
        filename: 'bundle.js',
    },
    module: {
        rules: [
            {
                test: /\.css$/i,
                include: resolve(__dirname, 'frontend'),
                use: ['style-loader', 'css-loader', 'postcss-loader'],
            },
        ],
    },
    plugins: [
        new WatchExternalFilesPlugin({
            files: [
                'src/main/resources/templates/**/*.html',
            ],
        }),
    ],
};
