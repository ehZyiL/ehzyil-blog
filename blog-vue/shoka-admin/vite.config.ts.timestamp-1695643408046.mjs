// vite.config.ts
import vue from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import path from "path";
import AutoImport from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/unplugin-auto-import/dist/vite.js";
import { ElementPlusResolver } from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/unplugin-vue-components/dist/resolvers.mjs";
import Components from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/unplugin-vue-components/dist/vite.mjs";
import { defineConfig } from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/vite/dist/node/index.js";
import { prismjsPlugin } from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/vite-plugin-prismjs/dist/index.js";
import { createSvgIconsPlugin } from "file:///D:/Study/Java/Code/blog-master/blog-vue/shoka-admin/node_modules/vite-plugin-svg-icons/dist/index.mjs";
var __vite_injected_original_dirname = "D:\\Study\\Java\\Code\\blog-master\\blog-vue\\shoka-admin";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    }),
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), "src/assets/icons")],
      symbolId: "icon-[dir]-[name]"
    }),
    prismjsPlugin({
      languages: ["java", "python", "go", "html", "json"],
      plugins: ["copy-to-clipboard"],
      theme: "okaidia",
      css: true
    })
  ],
  resolve: {
    alias: {
      "~": path.resolve(__vite_injected_original_dirname, "./"),
      "@": path.resolve(__vite_injected_original_dirname, "./src")
    },
    extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".vue"]
  },
  server: {
    open: true,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path2) => path2.replace(/^\/api/, "")
      }
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxTdHVkeVxcXFxKYXZhXFxcXENvZGVcXFxcYmxvZy1tYXN0ZXJcXFxcYmxvZy12dWVcXFxcc2hva2EtYWRtaW5cIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkQ6XFxcXFN0dWR5XFxcXEphdmFcXFxcQ29kZVxcXFxibG9nLW1hc3RlclxcXFxibG9nLXZ1ZVxcXFxzaG9rYS1hZG1pblxcXFx2aXRlLmNvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRDovU3R1ZHkvSmF2YS9Db2RlL2Jsb2ctbWFzdGVyL2Jsb2ctdnVlL3Nob2thLWFkbWluL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHZ1ZSBmcm9tIFwiQHZpdGVqcy9wbHVnaW4tdnVlXCI7XG5pbXBvcnQgcGF0aCBmcm9tIFwicGF0aFwiO1xuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSBcInVucGx1Z2luLWF1dG8taW1wb3J0L3ZpdGVcIjtcbmltcG9ydCB7IEVsZW1lbnRQbHVzUmVzb2x2ZXIgfSBmcm9tIFwidW5wbHVnaW4tdnVlLWNvbXBvbmVudHMvcmVzb2x2ZXJzXCI7XG5pbXBvcnQgQ29tcG9uZW50cyBmcm9tIFwidW5wbHVnaW4tdnVlLWNvbXBvbmVudHMvdml0ZVwiO1xuaW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSBcInZpdGVcIjtcbmltcG9ydCB7IHByaXNtanNQbHVnaW4gfSBmcm9tIFwidml0ZS1wbHVnaW4tcHJpc21qc1wiO1xuaW1wb3J0IHsgY3JlYXRlU3ZnSWNvbnNQbHVnaW4gfSBmcm9tIFwidml0ZS1wbHVnaW4tc3ZnLWljb25zXCI7XG5cbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICBwbHVnaW5zOiBbXG4gICAgdnVlKCksXG4gICAgQXV0b0ltcG9ydCh7XG4gICAgICByZXNvbHZlcnM6IFtFbGVtZW50UGx1c1Jlc29sdmVyKCldLFxuICAgIH0pLFxuICAgIENvbXBvbmVudHMoe1xuICAgICAgcmVzb2x2ZXJzOiBbRWxlbWVudFBsdXNSZXNvbHZlcigpXSxcbiAgICB9KSxcbiAgICBjcmVhdGVTdmdJY29uc1BsdWdpbih7XG4gICAgICAvLyBcdTYzMDdcdTVCOUFcdTk3MDBcdTg5ODFcdTdGMTNcdTVCNThcdTc2ODRcdTU2RkVcdTY4MDdcdTY1ODdcdTRFRjZcdTU5MzlcbiAgICAgIGljb25EaXJzOiBbcGF0aC5yZXNvbHZlKHByb2Nlc3MuY3dkKCksIFwic3JjL2Fzc2V0cy9pY29uc1wiKV0sXG4gICAgICAvLyBcdTYzMDdcdTVCOUFzeW1ib2xJZFx1NjgzQ1x1NUYwRlxuICAgICAgc3ltYm9sSWQ6IFwiaWNvbi1bZGlyXS1bbmFtZV1cIixcbiAgICB9KSxcbiAgICBwcmlzbWpzUGx1Z2luKHtcbiAgICAgIGxhbmd1YWdlczogW1wiamF2YVwiLCBcInB5dGhvblwiLCBcImdvXCIsIFwiaHRtbFwiLCBcImpzb25cIl0sXG4gICAgICBwbHVnaW5zOiBbXCJjb3B5LXRvLWNsaXBib2FyZFwiXSxcbiAgICAgIHRoZW1lOiBcIm9rYWlkaWFcIixcbiAgICAgIGNzczogdHJ1ZSxcbiAgICB9KSxcbiAgXSxcbiAgcmVzb2x2ZToge1xuICAgIC8vIGh0dHBzOi8vY24udml0ZWpzLmRldi9jb25maWcvI3Jlc29sdmUtYWxpYXNcbiAgICBhbGlhczoge1xuICAgICAgLy8gXHU4QkJFXHU3RjZFXHU4REVGXHU1Rjg0XG4gICAgICBcIn5cIjogcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgXCIuL1wiKSxcbiAgICAgIC8vIFx1OEJCRVx1N0Y2RVx1NTIyQlx1NTQwRFxuICAgICAgXCJAXCI6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIFwiLi9zcmNcIiksXG4gICAgfSxcbiAgICAvLyBodHRwczovL2NuLnZpdGVqcy5kZXYvY29uZmlnLyNyZXNvbHZlLWV4dGVuc2lvbnNcbiAgICBleHRlbnNpb25zOiBbXCIubWpzXCIsIFwiLmpzXCIsIFwiLnRzXCIsIFwiLmpzeFwiLCBcIi50c3hcIiwgXCIuanNvblwiLCBcIi52dWVcIl0sXG4gIH0sXG4gIHNlcnZlcjoge1xuICAgIG9wZW46IHRydWUsXG4gICAgcHJveHk6IHtcbiAgICAgIFwiL2FwaVwiOiB7XG4gICAgICAgIHRhcmdldDogXCJodHRwOi8vbG9jYWxob3N0OjgwODBcIixcbiAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxuICAgICAgICByZXdyaXRlOiAocGF0aCkgPT4gcGF0aC5yZXBsYWNlKC9eXFwvYXBpLywgXCJcIiksXG4gICAgICB9LFxuICAgIH0sXG4gIH0sXG59KTtcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBeVYsT0FBTyxTQUFTO0FBQ3pXLE9BQU8sVUFBVTtBQUNqQixPQUFPLGdCQUFnQjtBQUN2QixTQUFTLDJCQUEyQjtBQUNwQyxPQUFPLGdCQUFnQjtBQUN2QixTQUFTLG9CQUFvQjtBQUM3QixTQUFTLHFCQUFxQjtBQUM5QixTQUFTLDRCQUE0QjtBQVByQyxJQUFNLG1DQUFtQztBQVV6QyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMxQixTQUFTO0FBQUEsSUFDUCxJQUFJO0FBQUEsSUFDSixXQUFXO0FBQUEsTUFDVCxXQUFXLENBQUMsb0JBQW9CLENBQUM7QUFBQSxJQUNuQyxDQUFDO0FBQUEsSUFDRCxXQUFXO0FBQUEsTUFDVCxXQUFXLENBQUMsb0JBQW9CLENBQUM7QUFBQSxJQUNuQyxDQUFDO0FBQUEsSUFDRCxxQkFBcUI7QUFBQSxNQUVuQixVQUFVLENBQUMsS0FBSyxRQUFRLFFBQVEsSUFBSSxHQUFHLGtCQUFrQixDQUFDO0FBQUEsTUFFMUQsVUFBVTtBQUFBLElBQ1osQ0FBQztBQUFBLElBQ0QsY0FBYztBQUFBLE1BQ1osV0FBVyxDQUFDLFFBQVEsVUFBVSxNQUFNLFFBQVEsTUFBTTtBQUFBLE1BQ2xELFNBQVMsQ0FBQyxtQkFBbUI7QUFBQSxNQUM3QixPQUFPO0FBQUEsTUFDUCxLQUFLO0FBQUEsSUFDUCxDQUFDO0FBQUEsRUFDSDtBQUFBLEVBQ0EsU0FBUztBQUFBLElBRVAsT0FBTztBQUFBLE1BRUwsS0FBSyxLQUFLLFFBQVEsa0NBQVcsSUFBSTtBQUFBLE1BRWpDLEtBQUssS0FBSyxRQUFRLGtDQUFXLE9BQU87QUFBQSxJQUN0QztBQUFBLElBRUEsWUFBWSxDQUFDLFFBQVEsT0FBTyxPQUFPLFFBQVEsUUFBUSxTQUFTLE1BQU07QUFBQSxFQUNwRTtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sT0FBTztBQUFBLE1BQ0wsUUFBUTtBQUFBLFFBQ04sUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsU0FBUyxDQUFDQSxVQUFTQSxNQUFLLFFBQVEsVUFBVSxFQUFFO0FBQUEsTUFDOUM7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbInBhdGgiXQp9Cg==
