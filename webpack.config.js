const { resolve } = require('path');
const WatchExternalFilesPlugin = require('webpack-watch-files-plugin').default;
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const assets = 'src/main/resources/static/build';

module.exports = {
    mode: 'development',
    entry: {
        bundle: './frontend/index.js',
    },
    output: {
        path: resolve(__dirname, assets),
        filename: '[name].js',
        library: {
            name: 'prostLib',
            type: 'var',
        },
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: 'bundle.css',
        }),
        new WatchExternalFilesPlugin({
            files: ['src/main/resources/templates/**/*.html'],
        }),
        new CssMinimizerPlugin(),
    ],
    module: {
        rules: [
            {
                test: /\.css$/i,
                include: resolve(__dirname, 'frontend'),
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    'postcss-loader',
                ],
                sideEffects: true,
            },
        ],
    },
};
