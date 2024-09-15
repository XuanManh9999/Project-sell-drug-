import axios from "axios";
import URL from "./utils/utils-url";
const AxiosConFig = axios.create({
  baseURL: URL.SERVER_LOCAL,
});

export default AxiosConFig;
