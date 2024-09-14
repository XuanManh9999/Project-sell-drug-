import AxiosConFig from "../axiosConfig";

const testFetchData = async () => {
  try {
    const response = await AxiosConFig.get("/user");
    return response.data;
  } catch (error) {
    console.error("Error from testFetchData", error);
  }
};

export { testFetchData };
