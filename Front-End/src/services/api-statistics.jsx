import AxiosConFig from "../axiosConfig";

const getStatistics = async () => {
  let response;
  try {
    response = await AxiosConFig.get("statistics");
    return response?.data || {};
  } catch (error) {
      
    return error.response?.data;
  }
};

export { getStatistics };
